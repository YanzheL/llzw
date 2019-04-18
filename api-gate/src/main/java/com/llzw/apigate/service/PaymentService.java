package com.llzw.apigate.service;

import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.error.ApiServiceException;
import java.util.Map;

public interface PaymentService {

  Payment create(User payer, Long orderId, float totalAmount,
      String subject, String description) throws ApiServiceException;

  boolean verify(Map<String, String> params) throws ApiServiceException;

}
