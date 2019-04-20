package com.llzw.apigate.web.util;

import com.llzw.apigate.service.error.RestApiErrorMessage;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class RestApiResponse {

  @Setter(AccessLevel.NONE)
  private String responseId = UUID.randomUUID().toString();

  @Setter(AccessLevel.NONE)
  private Date timestamp;

  private boolean success;

  private Object data;

  private RestApiErrorMessage error;

  public RestApiResponse(boolean success, Object data, RestApiErrorMessage error) {
    this.success = success;
    this.data = data;
    this.error = error;
    this.timestamp = new Date();
  }
}
