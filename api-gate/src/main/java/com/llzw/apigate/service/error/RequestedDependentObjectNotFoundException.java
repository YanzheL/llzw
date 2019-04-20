package com.llzw.apigate.service.error;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class RequestedDependentObjectNotFoundException extends RestApiException {

  private final static String type = "REQUESTED_DEPENDENT_OBJECT_NOT_FOUND";

  public RequestedDependentObjectNotFoundException(String message) {
    super(message);
  }

  @Override
  public String getType() {
    return super.getType() + "." + type;
  }
}
