package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestPaymentException extends RestApiException {

  private final static String type = "PAYMENT_SERVICE";

  public RestPaymentException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
