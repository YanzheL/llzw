package com.llzw.apigate.service;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.llzw.apigate.persistence.dao.FileMetaDataRepository;
import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.web.dto.FileContainer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

  private HashFunction hashFunction;

  private boolean checked;

  public UniqueHashFileStorageService() {
    hashFunction = Hashing.sha256();
  }

  // File validation is already done in FileValidator, so we just save it now.
  @Override
  public Optional<FileMetaData> save(FileContainer file) throws IOException {
    byte[] content = file.getFile().getBytes();
    String mimeType = file.getMimeType();
    hashFunction.hashBytes(content);
    String hashCode = Hashing.sha256().hashBytes(content).toString();
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
    checkBasePathIfNotExist();
    try (OutputStream dst = new FileOutputStream(basePath + File.separator + hashCode)) {
      dst.write(content);
    } catch (Exception e) {
      fileMetaDataRepository.delete(metaData);
      throw e;
    }

    return Optional.of(saveResult);
  }

  @Override
  public Resource load(String path) {
    return null;
  }

  @Override
  public void delete(String path) throws IOException {
    Optional<FileMetaData> fileMetaDataOptional = fileMetaDataRepository.findByHash(path);
    if (!fileMetaDataOptional.isPresent()) {
      return;
    }

    FileMetaData fileMetaData = fileMetaDataOptional.get();
    if (fileMetaData.decreaseReferenceCount()) {
      // Reference count reaches zero, so we delete the underlying file.
      Path filePath = Paths.get(basePath + File.separator + fileMetaData.getHash());
      Files.deleteIfExists(filePath);
    }
    fileMetaDataRepository.save(fileMetaData);
  }

  private void checkBasePathIfNotExist() {
    if (!checked) {
      new File(basePath).mkdirs();
      checked = true;
    }
  }
}
