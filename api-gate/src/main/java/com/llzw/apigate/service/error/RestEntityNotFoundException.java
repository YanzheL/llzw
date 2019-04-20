package com.llzw.apigate.service.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RestEntityNotFoundException extends RestApiException {

  private final static String type = "ENTITY_NOT_FOUND";

  public RestEntityNotFoundException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
