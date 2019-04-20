package com.llzw.apigate.message;

import com.llzw.apigate.message.error.RestApiErrorMessage;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class RestResponseFactory {

//  public static ResponseEntity<RestApiResponseMessage> success(
//      final Object data, boolean success, HttpStatus status) {
//    return new ResponseEntity<>(new RestApiResponse(success, data, null), status);
//  }

  public static ResponseEntity<Object> success(final Object data) {
    return ResponseEntity.ok(new RestApiResponse(true, data, null));
  }

  public static ResponseEntity<Object> success(
      final Object data,
      HttpStatus status
  ) {
    return new ResponseEntity<>(new RestApiResponse(true, data, null), status);
  }

//  public static ResponseEntity<RestApiResponseMessage> success(final Object data,
//      boolean success) {
//    return ResponseEntity.ok(new RestApiResponse(success, data, null));
//  }

  public static ResponseEntity<Object> error(
      RestApiErrorMessage error,
      HttpStatus status
  ) {
    return new ResponseEntity<>(new RestApiResponse(false, null, error), status);
  }
}
