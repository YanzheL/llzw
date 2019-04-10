package com.llzw.apigate.service;


import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.web.dto.FileContainer;
import java.io.IOException;
import java.util.Optional;
import org.springframework.core.io.Resource;

public interface FileStorageService {

  /**
   * Perform <tt>save</tt> operation on the target file. The actual action of <tt>save</tt> is
   * implementation-defined.
   *
   * @param file The validated file container uploaded from client.
   */
  Optional<FileMetaData> save(FileContainer file) throws IOException;

  Resource load(String path);

  /**
   * Perform delete operation on the target file. The actual action of <tt>delete</tt> is
   * implementation-defined.
   *
   * @param path The Unique-Identifier of a file resource.
   */
  void delete(String path) throws IOException;

}
