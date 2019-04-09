package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.FileMetaData;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.repository.CrudRepository;

public interface FileMetaDataRepository extends CrudRepository<FileMetaData, Long> {

  Optional<FileMetaData> findByHash(String hash);

  Stream<FileMetaData> findAllByReferrenceCountEquals(int count);
}
