package com.llzw.apigate.message;

import com.llzw.apigate.message.error.RestApiErrorMessage;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class RestApiResponse<T> implements RestApiResponseMessage {

  @Setter(AccessLevel.NONE)
  protected String responseId = UUID.randomUUID().toString();

  @Setter(AccessLevel.NONE)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  protected Date timestamp;

  protected boolean success;

  protected T data;

  protected RestApiErrorMessage error;

  public RestApiResponse(boolean success, T data, RestApiErrorMessage error) {
    this.timestamp = new Date();
    this.success = success;
    this.data = data;
    this.error = error;
  }
}
