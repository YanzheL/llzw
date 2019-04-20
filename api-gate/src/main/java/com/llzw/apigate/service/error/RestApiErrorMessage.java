package com.llzw.apigate.service.error;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonGetter;

@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public interface RestApiErrorMessage {

  @JsonGetter("type")
  String getType();

  @JsonGetter("message")
  String getMessage();
}
