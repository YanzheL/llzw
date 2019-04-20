package com.llzw.apigate.service.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestAccessDeniedException extends RestSecurityException {

  private final static String type = "ACCESS_DENIED";

  public RestAccessDeniedException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
