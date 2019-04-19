package com.llzw.apigate.service.error;

public class RestAccessDeniedException extends RestSecurityException {

  private final String errorCode = "ACCESS_DENIED";

  public RestAccessDeniedException() {
  }

  public RestAccessDeniedException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return super.getErrorCode() + "." + errorCode;
  }
}
