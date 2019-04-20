package com.llzw.apigate.message.error;

import lombok.NoArgsConstructor;

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
}
