package com.llzw.apigate.message;

import com.llzw.apigate.message.error.RestApiErrorMessage;
import java.util.Date;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class RestApiResponse implements RestApiResponseMessage {

  @Setter(AccessLevel.NONE)
  private String responseId = UUID.randomUUID().toString();

  @Setter(AccessLevel.NONE)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
