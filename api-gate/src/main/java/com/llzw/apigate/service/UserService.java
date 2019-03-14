package com.llzw.apigate.service;

import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.RealNameVerificationDto;
import com.llzw.apigate.web.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.persistence.EntityExistsException;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

  User register(UserDto userDto) throws EntityExistsException;

  Optional<User> findUserByUsername(String username);

  Optional<User> findUserByEmail(String email);

  boolean changeUserPassword(String username, String password);

  void realNameVerification(String username, RealNameVerificationDto realNameVerificationDto);

  List<User> getUsersFromSessionRegistry();
}
