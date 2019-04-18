package com.llzw.apigate.service.error;

public abstract class ApiServiceException extends Exception {

  public ApiServiceException() {
  }

  public ApiServiceException(String message) {
    super(message);
  }

  public abstract String getErrorCode();
}
