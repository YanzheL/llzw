package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InvalidRestParameterException extends RestApiException {

  private final static String type = "INVALID_PARAMETER";

  public InvalidRestParameterException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
