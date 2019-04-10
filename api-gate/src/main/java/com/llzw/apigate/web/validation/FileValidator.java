package com.llzw.apigate.web.validation;

import com.llzw.apigate.web.dto.FileContainer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FileValidator implements Validator {

  private static final long K = 1L << 10;
  private static final long M = K * K;
  private static final long G = M * K;
  private static final long T = G * K;
  // Key: MIME Type
  // Value: Size limit in Bytes
  private static Map<String, Long> typeContrains = new HashMap<>();

  static {
    typeContrains.put("image/jpeg", 10 * M);
    typeContrains.put("image/gif", 10 * M);
    typeContrains.put("image/png", 10 * M);
    typeContrains.put("image/svg+xml", 10 * M);

    typeContrains.put("audio/wav", 5 * M);
    typeContrains.put("audio/mpeg", 5 * M);

    typeContrains.put("video/mp4", 100 * M);
    typeContrains.put("video/x-msvideo", 100 * M);
    typeContrains.put("video/mpeg", 100 * M);

    typeContrains.put("application/pdf", 20 * M);
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return FileContainer.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    FileContainer fileContainer = (FileContainer) target;
    boolean typeValid = false;
    boolean sizeValid = false;
    try {
      String type = fileContainer.getMimeType();
      for (Map.Entry<String, Long> entry : typeContrains.entrySet()) {
        if (!entry.getKey().equals(type)) {
          continue;
        }
        typeValid = true;
        if (fileContainer.getFile().getSize() <= entry.getValue()) {
          sizeValid = true;
          break;
        }
      }
      if (!typeValid) {
        errors.reject("Invalid file type");
        return;
      }
      if (!sizeValid) {
        errors.reject("Invalid file size");
      }
    } catch (IOException e) {
      errors.reject(
          String.format("IOException raised while processing file, message = %s", e.getMessage()));
    }
  }
}
