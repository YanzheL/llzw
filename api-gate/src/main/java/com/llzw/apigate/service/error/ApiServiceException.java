package com.llzw.apigate.service.error;

public class ApiServiceException extends Exception {

  private final String errorCode = "API";

  public ApiServiceException() {
  }

  public ApiServiceException(String message) {
    super(message);
  }

  public String getErrorCode() {
    return errorCode;
  }
}
