package com.llzw.apigate.service;


import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.web.dto.FileContainer;
import java.util.Collection;
import java.util.Optional;
import org.springframework.core.io.Resource;

public interface StorageService {

  Optional<FileMetaData> save(FileContainer file, Collection<String> msgs);

  Resource load(String path);

  void delete(String path);

}
