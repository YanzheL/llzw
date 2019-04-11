package com.llzw.apigate.service;


import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.web.dto.FileDto;
import java.io.IOException;
import java.util.Optional;

public interface FileStorageService {

  /**
   * Perform <tt>save</tt> operation on the target file. The actual action of <tt>save</tt> is
   * implementation-defined.
   *
   * @param file The validated file container uploaded from client.
   */
  Optional<FileMetaData> save(FileDto file) throws IOException;

  FileDto load(String path) throws IOException;

  /**
   * Perform delete operation on the target file. The actual action of <tt>delete</tt> is
   * implementation-defined.
   *
   * @param path The Unique-Identifier of a file resource.
   */
  void delete(String path);

}
