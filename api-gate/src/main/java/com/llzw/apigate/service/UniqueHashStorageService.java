package com.llzw.apigate.service;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.llzw.apigate.persistence.dao.FileMetaDataRepository;
import com.llzw.apigate.persistence.entity.FileMetaData;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import lombok.Setter;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public class UniqueHashStorageService implements StorageService {

  @Value("${storage.basepath:storage}")
  private String basePath;

  @Setter(onMethod_ = @Autowired)
  private FileMetaDataRepository fileMetaDataRepository;

  public UniqueHashStorageService() {
  }

  public static boolean validateMimeType(String type) {
    return true;
  }

  @Override
  public Optional<FileMetaData> save(MultipartFile file, Collection<String> msgs) {
    Resource resource = file.getResource();
    if (!resource.isReadable()) {
      msgs.add("Resource is not readable");
      return Optional.empty();
    }
    File underlyingFile;
    try {
      underlyingFile = resource.getFile();
    } catch (IOException e) {
      msgs.add("IOException raised while reading file");
      return Optional.empty();
    }

    try {
      Tika tika = new Tika();
      String mimeType = tika.detect(underlyingFile);
      if (!validateMimeType(mimeType)) {
        msgs.add("Invalid MIME type");
        return Optional.empty();
      }
      String hashCode = Files.hash(underlyingFile, Hashing.sha256()).toString();
      Optional<FileMetaData> fileMetaDataOptional = fileMetaDataRepository.findByHash(hashCode);
      if (fileMetaDataOptional.isPresent()) {
        return fileMetaDataOptional;
      }
      FileMetaData metaData = new FileMetaData();
      metaData.setHash(hashCode);
      metaData.setMimetype(mimeType);
      FileMetaData saveResult = fileMetaDataRepository.save(metaData);
      Files.copy(underlyingFile, new File(basePath + File.separator + hashCode));
      return Optional.of(saveResult);
    } catch (IOException e) {
      msgs.add("IOException raised while saving file");
      return Optional.empty();
    }
  }

  @Override
  public Resource load(String path) {
    return null;
  }

  @Override
  public void delete(String path) {

  }
}
