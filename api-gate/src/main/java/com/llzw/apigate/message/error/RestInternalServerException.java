package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class RestInternalServerException extends RestApiException {

  private final static String type = "INTERNAL_SERVER_ERROR";

  public RestInternalServerException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }

  @Override
  public HttpStatus suggestHttpStatus() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
