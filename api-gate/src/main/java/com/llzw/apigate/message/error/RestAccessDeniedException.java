package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

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

  @Override
  public HttpStatus suggestHttpStatus() {
    return HttpStatus.FORBIDDEN;
  }
}
