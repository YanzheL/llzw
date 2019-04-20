package com.llzw.apigate.service;

import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.PaymentRepository;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.Payment.PaymentStatusType;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.error.PaymentException;
import com.llzw.apigate.service.error.RequestedDependentObjectNotFoundException;
import com.llzw.apigate.service.error.RestApiException;
import com.llzw.apigate.service.error.TradeNotFoundPaymentVendorException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SimplePaymentService implements PaymentService {

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
  public Payment create(User payer, Long orderId,
      String subject, String description)
      throws RestApiException {
    Optional<Order> orderOptional = orderRepository.findById(orderId);
    if (!orderOptional.isPresent()) {
      throw new RequestedDependentObjectNotFoundException(
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
    Optional<Payment> paymentOptional = paymentRepository.findById(paymentId);
    if (!paymentOptional.isPresent()) {
      throw new RequestedDependentObjectNotFoundException(
          String.format("Target payment <%s> does not exist", paymentId));
    }
    Payment payment = paymentOptional.get();
    if (payment.isConfirmed()) {
      throw new PaymentException(
          String.format("Target payment <%s> is already confirmed", paymentId));
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

    Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
    if (!paymentOptional.isPresent()) {
      throw new RequestedDependentObjectNotFoundException("Target payment does not exist.");
    }
    Payment payment = paymentOptional.get();
    Order targetOrder = payment.getOrder();
    if (Math.abs(payment.getTotalAmount() - targetOrder.getTotalAmount()) > 0.001) {
      throw new PaymentException("Payment amount mismatch");
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
      Map<String, String> result = vendor.query(payment.getOrder().getId());
      String tradeStatue = result.get("trade_status");
      if (tradeStatue.equals("TRADE_SUCCESS") || tradeStatue.equals("TRADE_FINISHED")) {
        return true;
      }
    } catch (TradeNotFoundPaymentVendorException e) {
      Date expire = calculateExpireDate(payment.getCreatedAt(), 15);
      if (new Date().after(expire)) {
        paymentRepository.delete(payment);
      }
      return false;
    }
    return false;
  }
}
