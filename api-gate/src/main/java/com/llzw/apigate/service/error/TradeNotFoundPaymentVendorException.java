package com.llzw.apigate.service.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TradeNotFoundPaymentVendorException extends PaymentVendorException {

  private final static String type = "TRADE_NOT_FOUND";

  public TradeNotFoundPaymentVendorException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
