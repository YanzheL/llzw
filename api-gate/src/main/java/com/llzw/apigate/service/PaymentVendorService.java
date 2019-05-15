package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.Payment;
import java.util.Map;

public interface PaymentVendorService {

  String pay(Payment payment) throws RestApiException;

  boolean verifySignature(Map<String, String> params);

  Map<String, String> query(String orderId) throws RestApiException;

}
