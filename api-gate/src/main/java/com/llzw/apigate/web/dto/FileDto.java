package com.llzw.apigate.web.dto;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Getter;
import lombok.Setter;
import org.apache.tika.Tika;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartFile;

public class FileDto {

  @Getter
  private Resource file;

  @Setter
  private String mimeType = "";

  @Setter
  private boolean typeDetermined;

  public void setFile(MultipartFile file) {
    this.file = file.getResource();
  }

  public void setFileFromPath(String path) throws MalformedURLException {
    Path p = Paths.get(path);
    this.file = new UrlResource(p.toUri());
  }

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
