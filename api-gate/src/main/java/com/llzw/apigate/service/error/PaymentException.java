package com.llzw.apigate.service.error;

public class PaymentException extends ApiServiceException {

  private final String errorCode = "PAYMENT_EXCEPTION";

  public PaymentException() {
  }

  public PaymentException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return errorCode;
  }
}
