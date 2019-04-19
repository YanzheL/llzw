package com.llzw.apigate.service;

import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.error.RestApiException;
import java.util.Map;

public interface PaymentService {

  Payment create(User payer, Long orderId,
      String subject, String description) throws RestApiException;

  Payment retry(Long paymentId) throws RestApiException;

  boolean verify(Map<String, String> params) throws RestApiException;

  boolean verify(Payment payment) throws RestApiException;

}
