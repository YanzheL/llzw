package com.llzw.apigate.service;

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
import java.util.Optional;
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
  public Payment create(User payer, String orderId,
      String subject, String description)
      throws RestApiException {
    Optional<Order> orderOptional = orderRepository.findById(UUID.fromString(orderId));
    if (!orderOptional.isPresent()) {
      throw new RestDependentEntityNotFoundException(
          String.format("Order <%s> do not exist", orderId));
    }
    Order targetOrder = orderOptional.get();
    Payment payment = new Payment();
    payment.setPayer(payer);
    payment.setOrder(targetOrder);
    payment.setSubject(subject);
    payment.setDescription(description);
    payment.setTotalAmount(targetOrder.getTotalAmount());
    payment.setStatus(PaymentStatusType.PENDING);
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
  public boolean verify(Map<String, String> params) throws RestApiException {
    if (!vendor.verifySignature(params)) {
      return false;
    }
    //商户订单号
    Long orderId = Long.valueOf(params.get("out_trade_no"));
    //支付宝交易号
    String trade_no = params.get("trade_no");
    //交易状态
    String trade_status = params.get("trade_status");

    if (!(trade_status.equals("TRADE_SUCCESS") || trade_status.equals("TRADE_FINISHED"))) {
      LOGGER.warn(String.format("Unexpected payment status <%s>", trade_status));
      return false;
    }
    Payment payment = paymentRepository.findByOrderId(orderId)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Target Payment for Order <%s> does not exist", orderId)));
    Order targetOrder = payment.getOrder();
    if (Math.abs(payment.getTotalAmount() - targetOrder.getTotalAmount()) > 0.001) {
      throw new RestPaymentException("Payment amount mismatch");
    }
    payment.setConfirmed(true);
    payment.setConfirmedAt(new Date());
    payment.setVendorTradeId(trade_no);
    payment.setStatus(PaymentStatusType.CONFIRMED);
    targetOrder.setPaid(true);
    paymentRepository.save(payment);
    return true;
  }

  @Override
  public boolean verify(Payment payment) throws RestApiException {
    try {
      Map<String, String> result = vendor.query(payment.getOrder().getId().toString());
      verify(result);
    } catch (RestTradeNotFoundPaymentVendorException e) {
      Date expire = calculateExpireDate(payment.getCreatedAt(), 15);
      if (new Date().after(expire)) {
        paymentRepository.delete(payment);
      }
      return false;
    }
    return false;
  }

  @Override
  public boolean verify(Long paymentId) throws RestApiException {
    Payment payment = paymentRepository.findById(paymentId)
        .orElseThrow(() -> new RestDependentEntityNotFoundException(
            String.format("Payment <%s> does not exist", paymentId)));
    return verify(payment);
  }
}
