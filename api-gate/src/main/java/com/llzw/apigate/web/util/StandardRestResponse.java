package com.llzw.apigate.web.util;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class StandardRestResponse {

  @Setter(AccessLevel.NONE)
  private String responseId = UUID.randomUUID().toString();

  private boolean success = true;

  private Object data;

  private ApiErrorMessage error;


  private StandardRestResponse(ApiErrorMessage error) {
    this.error = error;
    success = false;
    data = null;
  }

  private StandardRestResponse(final Object data) {
    this.data = data;
  }

  private StandardRestResponse(final Object data, boolean success) {
    this(data);
    this.success = success;
  }

  public static ResponseEntity<Object> getResponseEntity(
      final Object data, boolean success, HttpStatus status) {
    return new ResponseEntity<>(new StandardRestResponse(data, success), status);
  }

  public static ResponseEntity<Object> getResponseEntity(final Object data) {
    return ResponseEntity.ok(new StandardRestResponse(data, true));
  }

  public static ResponseEntity<Object> getResponseEntity(final Object data, boolean success) {
    return ResponseEntity.ok(new StandardRestResponse(data, success));
  }

  public static ResponseEntity<Object> errorResponseEntity(ApiErrorMessage message,
      HttpStatus status) {
    return new ResponseEntity<>(new StandardRestResponse(message), status);
  }

  public static ResponseEntity<Object> errorResponseEntity(Exception exception,
      HttpStatus status) {
    return errorResponseEntity(
        new ApiErrorMessage(exception.getClass().getName(), exception.getMessage()),
        status
    );
  }
}
