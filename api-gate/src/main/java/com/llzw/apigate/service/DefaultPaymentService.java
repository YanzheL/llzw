package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestAccessDeniedException;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestDependentEntityNotFoundException;
import com.llzw.apigate.message.error.RestPaymentException;
import com.llzw.apigate.message.error.RestTradeNotFoundPaymentVendorException;
import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.PaymentRepository;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.Payment.PaymentStatusType;
import com.llzw.apigate.persistence.entity.User;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DefaultPaymentService implements PaymentService {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Setter(onMethod_ = @Autowired)
  PaymentVendorService vendor;

  @Setter(onMethod_ = @Autowired)
  PaymentRepository paymentRepository;

  @Setter(onMethod_ = @Autowired)
  OrderRepository orderRepository;

  public static Date calculateExpireDate(Date date, int minutes) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.MINUTE, minutes);
    return calendar.getTime();
  }

  @Override
  public Payment create(User payer, String orderId, String subject, String description)
      throws RestApiException {
    Order targetOrder = orderRepository.findById(UUID.fromString(orderId)).orElseThrow(
        () -> new RestDependentEntityNotFoundException(
            String.format("Order <%s> do not exist", orderId))
    );
    Payment payment = new Payment();
    payment.setPayer(payer);
    payment.setOrder(targetOrder);
    payment.setSubject(subject);
    payment.setDescription(description);
    payment.setTotalAmount(targetOrder.getTotalAmount());
    payment.setStatus(PaymentStatusType.PENDING);
    payment.setValid(true);
    payment = paymentRepository.save(payment);
    String orderString = vendor.pay(payment);
    payment.setOrderString(orderString);
    return payment;
  }

  @Override
  public Payment retry(Long paymentId) throws RestApiException {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Payment <%s> does not exist", paymentId)));
    if (payment.isConfirmed()) {
      throw new RestPaymentException(
          String.format("Payment <%s> is already confirmed", paymentId));
    }
    String orderString = vendor.pay(payment);
    payment.setOrderString(orderString);
    return payment;
  }

  @Override
  public boolean verifyFromVendor(Map<String, String> params) throws RestApiException {
    if (!vendor.verifySignature(params)) {
      return false;
    }
    String orderId = params.get("out_trade_no");
    String tradeNo = params.get("trade_no");
    Payment payment = paymentRepository.findByOrderId(UUID.fromString(orderId))
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Target Payment for Order <%s> does not exist", orderId)));
    updateStatus(payment, tradeNo);
    return true;
  }

  @Override
  public boolean verify(Payment payment) throws RestApiException {
    try {
      Map<String, String> result = vendor.query(payment.getOrder().getId().toString());
      String tradeNo = result.get("trade_no");
      if (isSuccess(result)) {
        if (!payment.isConfirmed()) {
          updateStatus(payment, tradeNo);
        }
        return true;
      }
      return false;
    } catch (RestTradeNotFoundPaymentVendorException e) {
      LOGGER.warn(
          String.format("RestTradeNotFoundPaymentVendorException, Payment<%d>", payment.getId()));
      Date expire = calculateExpireDate(payment.getCreatedAt(), 15);
      if (new Date().after(expire)) {
        paymentRepository.delete(payment);
      }
      return false;
    }
  }

  @Override
  public boolean verify(Long paymentId) throws RestApiException {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Payment <%d> does not exist", paymentId)));
    return verify(payment);
  }

  @Override
  public Payment findById(User user, Long id) throws RestApiException {
    return paymentRepository.findByIdAndUser(id, user)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format(
                "Payment <%d> does not exist or you do not have access to this entity",
                id)));
  }

  @Override
  public Payment findByOrderId(User user, String orderId) throws RestApiException {
    return paymentRepository.findByOrderIdAndUser(UUID.fromString(orderId), user)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format(
                "No such payment with OrderId <%s>, or you do not have access to this entity",
                orderId)));
  }

  private Payment updateStatus(Payment payment, String tradeNo) throws RestApiException {
    Order targetOrder = payment.getOrder();
    if (Math.abs(payment.getTotalAmount() - targetOrder.getTotalAmount()) > 0.001) {
      throw new RestPaymentException("Payment amount mismatch");
    }
    payment.setConfirmed(true);
    payment.setConfirmedAt(new Date());
    payment.setVendorTradeId(tradeNo);
    payment.setStatus(PaymentStatusType.CONFIRMED);
    targetOrder.setPaid(true);
    return paymentRepository.save(payment);
  }

  private boolean isSuccess(Map<String, String> params) {
    //商户订单号
    String orderId = params.get("out_trade_no");
    //支付宝交易号
    String tradeNo = params.get("trade_no");
    //交易状态
    String trade_status = params.get("trade_status");
    if (!(trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_FINISHED"))) {
      LOGGER.warn(String.format("Unexpected payment status <%s>", trade_status));
      return false;
    }
    return true;
  }
}
