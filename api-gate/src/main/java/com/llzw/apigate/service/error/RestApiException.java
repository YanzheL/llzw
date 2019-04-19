package com.llzw.apigate.service.error;

import com.llzw.apigate.web.util.ApiErrorMessage;

public class RestApiException extends Exception {

  private final String errorCode = "API";

  public RestApiException() {
  }

  public RestApiException(String message) {
    super(message);
  }

  public String getErrorCode() {
    return errorCode;
  }

  public ApiErrorMessage getApiErrorMessage() {
    return new ApiErrorMessage(getErrorCode(), getMessage());
  }
}
