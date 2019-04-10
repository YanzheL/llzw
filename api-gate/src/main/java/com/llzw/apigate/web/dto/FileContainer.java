package com.llzw.apigate.web.dto;

import java.io.IOException;
import lombok.Getter;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

public class FileContainer {

  @Getter
  private MultipartFile file;

  private String mimeType;

  private boolean typeDetermined;

  public String getMimeType() throws IOException {
    if (!typeDetermined) {
      Tika tika = new Tika();
      mimeType = tika.detect(file.getResource().getFile());
      typeDetermined = true;
    }
    return mimeType;
  }

}
