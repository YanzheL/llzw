package com.llzw.apigate.service;

import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.web.dto.RealNameVerificationDto;
import com.llzw.apigate.web.dto.UserDto;
import com.llzw.apigate.web.dto.UserPatchDto;
import java.util.List;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  User register(UserDto userDto) throws RestApiException;

  User updateInfo(String username, UserPatchDto userPatchDto) throws RestApiException;

  User setUserPassword(String username, String password)
      throws RestApiException;

  User updateUserPassword(
      String username, String oldPassword, String newPassword)
      throws RestApiException;

  User realNameVerification(
      String username, RealNameVerificationDto realNameVerificationDto)
      throws RestApiException;

  List<User> getUsersFromSessionRegistry();
}
