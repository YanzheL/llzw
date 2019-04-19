package com.llzw.apigate.service.error;

public class PaymentException extends RestApiException {

  private final String errorCode = "PAYMENT_SERVICE";

  public PaymentException() {
  }

  public PaymentException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return super.getErrorCode() + "." + errorCode;
  }
}
