package com.llzw.apigate.web.dto;

import java.io.IOException;
import lombok.Getter;
import lombok.Setter;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

public class FileContainer {

  @Getter
  @Setter
  private MultipartFile file;

  private String mimeType = "";

  private boolean typeDetermined;

  public String getMimeType() throws IOException {
    if (file == null) {
      throw new IOException("File cannot be null");
    }
    if (!typeDetermined) {
      Tika tika = new Tika();
      mimeType = tika.detect(file.getInputStream());
      typeDetermined = true;
    }
    return mimeType;
  }

}
