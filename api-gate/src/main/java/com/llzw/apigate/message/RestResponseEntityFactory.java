package com.llzw.apigate.message;

import com.llzw.apigate.message.error.RestApiErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestResponseEntityFactory {

  private RestResponseEntityFactory() {
  }

  public static ResponseEntity make(RestApiResponseMessage message, HttpStatus status) {
    return new ResponseEntity<>(message, status);
  }

  public static ResponseEntity success(final Object data) {
    return ResponseEntity.ok(new RestApiResponse(true, data, null));
  }

  public static ResponseEntity success(final Object data, HttpStatus status) {
    return new ResponseEntity<>(new RestApiResponse(true, data, null), status);
  }

  public static ResponseEntity<Object> error(RestApiErrorMessage error) {
    return new ResponseEntity<>(
        new RestApiResponse(false, null, error),
        error.suggestHttpStatus()
    );
  }

  public static ResponseEntity<Object> error(RestApiErrorMessage error, HttpStatus status) {
    return new ResponseEntity<>(new RestApiResponse(false, null, error), status);
  }
}
