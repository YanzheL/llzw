package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestUnsupportedOperationException extends RestApiException {

  private final static String type = "OPERATION_UNSUPPORTED";

  public RestUnsupportedOperationException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
