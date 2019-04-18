package com.llzw.apigate.service.error;

public class RequestedDependentObjectNotFoundException extends ApiServiceException {

  private final String errorCode = "REQUESTED_DEPENDENT_OBJECT_NOT_FOUND";

  public RequestedDependentObjectNotFoundException() {
  }

  public RequestedDependentObjectNotFoundException(String message) {
    super(message);
  }

  public RequestedDependentObjectNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public RequestedDependentObjectNotFoundException(Throwable cause) {
    super(cause);
  }

  @Override
  public String getErrorCode() {
    return errorCode;
  }
}
