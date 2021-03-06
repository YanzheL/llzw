package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.User;
import java.util.Map;

public interface PaymentService {

  Payment create(User payer, String orderId,
      String subject, String description) throws RestApiException;

  Payment retry(Long paymentId) throws RestApiException;

  boolean verifyFromVendor(Map<String, String> params) throws RestApiException;

  boolean verify(Payment payment) throws RestApiException;

  boolean verify(Long paymentId) throws RestApiException;

  Payment findByOrderId(User user, String orderId) throws RestApiException;

  Payment findById(User user, Long id) throws RestApiException;

}
