package com.llzw.apigate.service.error;

public class PaymentVendorException extends ApiServiceException {

  private final String errorCode = "PAYMENT_VENDOR_EXCEPTION";

  public PaymentVendorException() {
  }

  public PaymentVendorException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return errorCode;
  }
}
