package com.llzw.apigate.service;

import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.RealNameVerificationDto;
import com.llzw.apigate.web.dto.UserDto;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  boolean register(UserDto userDto, Collection<String> msgs);

  boolean setUserPassword(String username, String password, Collection<String> msgs);

  boolean updateUserPassword(
      String username, String oldPassword, String newPassword, Collection<String> msgs);

  boolean realNameVerification(
      String username, RealNameVerificationDto realNameVerificationDto, Collection<String> msgs);

  List<User> getUsersFromSessionRegistry();
}
