package com.llzw.apigate.service.error;

public class PaymentVendorException extends ApiServiceException {

  private final String errorCode = "PAYMENT_VENDOR_EXCEPTION";

  public PaymentVendorException() {
  }

  public PaymentVendorException(String message) {
    super(message);
  }

  public PaymentVendorException(String message, Throwable cause) {
    super(message, cause);
  }

  public PaymentVendorException(Throwable cause) {
    super(cause);
  }

  @Override
  public String getErrorCode() {
    return errorCode;
  }
}
