package com.llzw.apigate.service;

import com.llzw.apigate.persistence.entity.Payment;
import com.llzw.apigate.service.error.ApiServiceException;
import java.util.Map;

public interface PaymentVendorService {

  String pay(Payment payment) throws ApiServiceException;

  boolean verifySignature(Map<String, String> params);

  Map<String, String> query(Long orderId) throws ApiServiceException;

}
