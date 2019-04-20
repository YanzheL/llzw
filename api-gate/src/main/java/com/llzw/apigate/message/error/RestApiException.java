package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestApiException extends Exception implements RestApiErrorMessage {

  private final static String type = "API";

  public RestApiException(String message) {
    super(message);
  }

  public String getType() {
    return type;
  }
}
