package com.llzw.apigate.web.util;

import com.llzw.apigate.service.error.RestApiErrorMessage;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class StandardRestResponse {

  public static ResponseEntity<Object> getResponseEntity(
      final Object data, boolean success, HttpStatus status) {
    return new ResponseEntity<>(new RestApiResponse(success, data, null), status);
  }

  public static ResponseEntity<Object> getResponseEntity(final Object data) {
    return ResponseEntity.ok(new RestApiResponse(true, data, null));
  }

  public static ResponseEntity<Object> getResponseEntity(final Object data, boolean success) {
    return ResponseEntity.ok(new RestApiResponse(success, data, null));
  }

  public static ResponseEntity<Object> errorResponseEntity(RestApiErrorMessage error,
      HttpStatus status) {
    return new ResponseEntity<>(new RestApiResponse(false, null, error), status);
  }
}
