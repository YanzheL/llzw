package com.llzw.apigate.service;

import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.UserDto;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

public interface UserService {

  User register(UserDto accountDto) throws EntityExistsException;

  Optional<User> findUserByUsername(String username);

  Optional<User> findUserByEmail(String email);

  boolean changeUserPassword(String username, String password);

  List<User> getUsersFromSessionRegistry();
}
