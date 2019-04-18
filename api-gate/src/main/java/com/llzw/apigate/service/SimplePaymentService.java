package com.llzw.apigate.service;

import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.PaymentRepository;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.Payment.PaymentStatusType;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.error.ApiServiceException;
import com.llzw.apigate.service.error.RequestedDependentObjectNotFoundException;
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

  @Override
  public Payment create(User payer, Long orderId,
      float totalAmount, String subject, String description)
      throws ApiServiceException {
    Optional<Order> orderOptional = orderRepository.findById(orderId);
    if (!orderOptional.isPresent()) {
      throw new RequestedDependentObjectNotFoundException(
          String.format("Order <%s> do not exist", orderId));
    }
    Payment payment = new Payment();
    payment.setPayer(payer);
    payment.setOrder(orderOptional.get());
    payment.setSubject(subject);
    payment.setDescription(description);
    payment.setTotalAmount(totalAmount);
    payment.setStatus(PaymentStatusType.PENDING);
    payment = paymentRepository.save(payment);
    String orderString = vendor.pay(payment);
    payment.setOrderString(orderString);
    return payment;
  }

  @Override
  public boolean verify(Map<String, String> params) throws ApiServiceException {
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
      return false;
    }

    Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
    if (!paymentOptional.isPresent()) {
      throw new RequestedDependentObjectNotFoundException("Target payment does not exist.");
    }
    Payment payment = paymentOptional.get();
    payment.setConfirmed(true);
    payment.setConfirmedAt(new Date());
    payment.setVendorTradeId(trade_no);
    payment.getOrder().setPaid(true);
    paymentRepository.save(payment);
    return true;
  }
}
