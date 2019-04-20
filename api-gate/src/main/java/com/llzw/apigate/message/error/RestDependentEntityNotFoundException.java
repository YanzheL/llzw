package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
public class RestDependentEntityNotFoundException extends RestApiException {

  private final static String type = "DEPENDENT_ENTITY_NOT_FOUND";

  public RestDependentEntityNotFoundException(String message) {
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
