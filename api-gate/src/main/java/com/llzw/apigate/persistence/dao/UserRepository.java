package com.llzw.apigate.persistence.dao;

import com.llzw.apigate.persistence.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}
