package com.llzw.apigate.service.error;

public class RequestedDependentObjectNotFoundException extends RestApiException {

  private final String errorCode = "REQUESTED_DEPENDENT_OBJECT_NOT_FOUND";

  public RequestedDependentObjectNotFoundException() {
  }

  public RequestedDependentObjectNotFoundException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return super.getErrorCode() + "." + errorCode;
  }
}
