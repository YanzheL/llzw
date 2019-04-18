package com.llzw.apigate.service.error;

public abstract class ApiServiceException extends Exception {

  public ApiServiceException() {
  }

  public ApiServiceException(String message) {
    super(message);
  }

  public ApiServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ApiServiceException(Throwable cause) {
    super(cause);
  }

  public abstract String getErrorCode();
}
