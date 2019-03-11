package com.llzw.apigate.web.util;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Data
public class StandardRestResponse<D> {
  private D data;

  @Setter(AccessLevel.NONE)
  private String responseId = UUID.randomUUID().toString();

  private boolean success = true;

  public StandardRestResponse(final D data) {
    this.data = data;
  }

  public StandardRestResponse(final D data, boolean success) {
    this(data);
    this.success = success;
  }

  public static ResponseEntity<Object> getResponseEntity(
      final Object data, boolean success, HttpStatus status) {
    return new ResponseEntity<>(new StandardRestResponse<>(data, success), status);
  }
}
