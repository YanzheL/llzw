package com.llzw.apigate.service.error;

public class PaymentVendorException extends RestApiException {

  private final String errorCode = "PAYMENT_VENDOR_SERVICE";

  public PaymentVendorException() {
  }

  public PaymentVendorException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return super.getErrorCode() + "." + errorCode;
  }
}
