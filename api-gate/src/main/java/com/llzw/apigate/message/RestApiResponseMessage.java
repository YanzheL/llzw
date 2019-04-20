package com.llzw.apigate.message;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.llzw.apigate.message.error.RestApiErrorMessage;
import java.util.Date;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public interface RestApiResponseMessage {

  @JsonGetter("responseId")
  String getResponseId();

  @JsonGetter("timestamp")
  Date getTimestamp();

  @JsonGetter("success")
  boolean isSuccess();

  @JsonGetter("data")
  Object getData();

  @JsonGetter("error")
  RestApiErrorMessage getError();
}
