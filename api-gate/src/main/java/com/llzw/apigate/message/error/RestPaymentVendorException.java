package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestPaymentVendorException extends RestApiException {

  private final static String type = "PAYMENT_VENDOR_SERVICE";

  public RestPaymentVendorException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
