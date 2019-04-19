package com.llzw.apigate.service.error;

public class TradeNotFoundPaymentVendorException extends PaymentVendorException {

  private final String errorCode = "TRADE_NOT_FOUND";

  public TradeNotFoundPaymentVendorException() {
  }

  public TradeNotFoundPaymentVendorException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return super.getErrorCode() + "." + errorCode;
  }
}
