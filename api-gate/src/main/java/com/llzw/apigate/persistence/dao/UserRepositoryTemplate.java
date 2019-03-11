package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepositoryTemplate<U extends User> extends CrudRepository<U, String> {

  Optional<U> findByUsername(String username);

  Optional<U> findByEmail(String email);
}
