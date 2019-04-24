package com.llzw.apigate.service;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteStreams;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.message.error.RestInternalServerException;
import com.llzw.apigate.persistence.dao.FileMetaDataRepository;
import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.web.dto.FileDto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UniqueHashFileStorageService implements FileStorageService {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  public static final Pattern PATH_PATTERN = Pattern.compile("[a-z0-9]{64}");

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
  public FileMetaData save(FileDto file) throws RestApiException {
    try {
      byte[] content = ByteStreams.toByteArray(file.getFile().getInputStream());
      String mimeType = file.getMimeType();
      String hashCode = hashFunction.hashBytes(content).toString();
      Optional<FileMetaData> fileMetaDataOptional = fileMetaDataRepository.findByHash(hashCode);
      // If same file already exists, then we just return that file's metadata
      if (fileMetaDataOptional.isPresent()) {
        return fileMetaDataOptional.get();
      }
      FileMetaData metaData = new FileMetaData();
      metaData.setHash(hashCode);
      metaData.setMimeType(mimeType);
      // Save file metadata to database
      metaData = fileMetaDataRepository.save(metaData);
      // Save file to filesystem
      createBasePathIfNotExist();
      try (OutputStream dst = new FileOutputStream(basePath + File.separator + hashCode)) {
        dst.write(content);
        return metaData;
      } catch (Exception e) {
        // If something bad happened while writing file to filesystem, we should delete that metadata.
        fileMetaDataRepository.delete(metaData);
        throw e;
      }
    } catch (IOException e) {
      throw new RestInternalServerException(e.getMessage());
    }
  }

  @Override
  public FileDto load(String path) throws IOException {
    FileDto container = new FileDto();
    container.setFileFromPath(basePath + File.separator + path);
    return container;
  }

  @Override
  public void delete(String path) {
    if (!PATH_PATTERN.matcher(path).matches()) {
      return;
    }
    try {
      Optional<FileMetaData> fileMetaDataOptional = fileMetaDataRepository.findByHash(path);
      if (!fileMetaDataOptional.isPresent()) {
        Path filePath = Paths.get(basePath + File.separator + path);
        Files.deleteIfExists(filePath);
        return;
      }
      FileMetaData fileMetaData = fileMetaDataOptional.get();
      if (fileMetaData.decreaseReferenceCount()) {
        // Reference count reaches zero, so we delete the underlying file.
        Path filePath = Paths.get(basePath + File.separator + fileMetaData.getHash());
        Files.deleteIfExists(filePath);
        fileMetaDataRepository.delete(fileMetaData);
      } else {
        // Reference count is not zero, so we save the change to DB.
        fileMetaDataRepository.save(fileMetaData);
      }
    } catch (IOException e) {
      LOGGER.warn(String.format("IOException raised while deleting target %s", path));
    }
  }

  @Override
  public boolean increaseReferenceCount(String path) {
    if (!PATH_PATTERN.matcher(path).matches()) {
      return false;
    }
    Optional<FileMetaData> fileMetaDataOptional = fileMetaDataRepository.findByHash(path);
    if (!fileMetaDataOptional.isPresent()) {
      return false;
    }
    FileMetaData fileMetaData = fileMetaDataOptional.get();
    fileMetaData.increaseReferenceCount();
    return true;
  }

  private void createBasePathIfNotExist() {
    if (!checked) {
      new File(basePath).mkdirs();
      checked = true;
    }
  }
}
