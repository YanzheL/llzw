package com.llzw.apigate.service;


import com.llzw.apigate.persistence.entity.FileMetaData;
import com.llzw.apigate.web.dto.FileContainer;
import java.util.Collection;
import java.util.Optional;
import org.springframework.core.io.Resource;

public interface StorageService {

  /**
   * Perform <tt>save</tt> operation on the target file. The actual action of <tt>save</tt> is
   * implementation-defined.
   *
   * @param file The validated file container uploaded from client.
   */
  Optional<FileMetaData> save(FileContainer file, Collection<String> msgs);

  Resource load(String path);

  /**
   * Perform delete operation on the target file. The actual action of <tt>delete</tt> is
   * implementation-defined.
   *
   * @param path The Unique-Identifier of a file resource.
   */
  void delete(String path);

}
