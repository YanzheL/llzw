package com.llzw.apigate.service.error;

public class RestSecurityException extends RestApiException {

  private final String errorCode = "SECURITY";

  public RestSecurityException() {
  }

  public RestSecurityException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return super.getErrorCode() + "." + errorCode;
  }
}
