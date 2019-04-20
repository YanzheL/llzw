package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class RestTradeNotFoundPaymentVendorException extends RestPaymentVendorException {

  private final static String type = "TRADE_NOT_FOUND";

  public RestTradeNotFoundPaymentVendorException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }

  @Override
  public HttpStatus suggestHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }
}
