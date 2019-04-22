package com.llzw.apigate.service;


import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.web.dto.FileDto;
import java.io.IOException;

public interface FileStorageService {

  /**
   * Perform <tt>save</tt> operation on the target file. The actual action of <tt>save</tt> is
   * implementation-defined.
   *
   * @param file The validated file container uploaded from client.
   * @return
   */
  FileMetaData save(FileDto file) throws RestApiException;

  FileDto load(String path) throws IOException;

  boolean increaseReferenceCount(String path);

  /**
   * Perform delete operation on the target file. The actual action of <tt>delete</tt> is
   * implementation-defined.
   *
   * @param path The Unique-Identifier of a file resource.
   */
  void delete(String path);

}
