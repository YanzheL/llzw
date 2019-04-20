package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestInvalidCredentialException extends RestSecurityException {

  private final static String type = "INVALID_CREDENTIAL";

  public RestInvalidCredentialException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
