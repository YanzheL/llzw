package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestSecurityException extends RestApiException {

  private final static String type = "SECURITY";

  public RestSecurityException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
