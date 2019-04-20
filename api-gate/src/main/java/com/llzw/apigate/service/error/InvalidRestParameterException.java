package com.llzw.apigate.service.error;

public class InvalidRestParameterException extends RestApiException {

  private final String errorCode = "INVALID_PARAMETER";

  public InvalidRestParameterException() {
  }

  public InvalidRestParameterException(String message) {
    super(message);
  }

  @Override
  public String getErrorCode() {
    return super.getErrorCode() + "." + errorCode;
  }
}
