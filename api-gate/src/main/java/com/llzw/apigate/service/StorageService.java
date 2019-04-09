package com.llzw.apigate.service;


import com.llzw.apigate.persistence.entity.FileMetaData;
import java.util.Collection;
import java.util.Optional;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

  Optional<FileMetaData> save(MultipartFile file, Collection<String> msgs);

  Resource load(String path);

  void delete(String path);

}
