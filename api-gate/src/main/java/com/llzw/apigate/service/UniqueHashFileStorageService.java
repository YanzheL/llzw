package com.llzw.apigate.service;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.llzw.apigate.persistence.dao.FileMetaDataRepository;
import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.web.dto.FileContainer;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UniqueHashFileStorageService implements FileStorageService {

  @Value("${storage.basepath:storage}")
  private String basePath;

  @Setter(onMethod_ = @Autowired)
  private FileMetaDataRepository fileMetaDataRepository;

  public UniqueHashFileStorageService() {
  }

  // File validation is already done in FileValidator, so we just save it now.
  @Override
  public Optional<FileMetaData> save(FileContainer file, Collection<String> msgs) {

    try {
      File underlyingFile = file.getFile().getResource().getFile();
      String mimeType = file.getMimeType();
      String hashCode = Files.hash(underlyingFile, Hashing.sha256()).toString();
      Optional<FileMetaData> fileMetaDataOptional = fileMetaDataRepository.findByHash(hashCode);
      // If same file already exists, then we just return that file's metadata
      if (fileMetaDataOptional.isPresent()) {
        return fileMetaDataOptional;
      }
      FileMetaData metaData = new FileMetaData();
      metaData.setHash(hashCode);
      metaData.setMimetype(mimeType);
      // Save file metadata to database
      FileMetaData saveResult = fileMetaDataRepository.save(metaData);
      // Save file to filesystem
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
