package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestInvalidParameterException extends RestApiException {

  private final static String type = "INVALID_PARAMETER";

  public RestInvalidParameterException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
