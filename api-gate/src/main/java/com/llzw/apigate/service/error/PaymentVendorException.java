package com.llzw.apigate.service.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentVendorException extends RestApiException {

  private final static String type = "PAYMENT_VENDOR_SERVICE";

  public PaymentVendorException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
