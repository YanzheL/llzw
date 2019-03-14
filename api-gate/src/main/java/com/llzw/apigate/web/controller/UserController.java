package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.UserService;
import com.llzw.apigate.web.dto.RealNameVerificationDto;
import com.llzw.apigate.web.dto.UserDto;
import com.llzw.apigate.web.util.StandardRestResponse;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RepositoryRestController
@RequestMapping(value = "/users")
public class UserController {
  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Setter(onMethod_ = @Autowired)
  private UserService userService;

  @PostMapping(value = "")
  public ResponseEntity register(@Valid UserDto userDto) {
    LOGGER.debug("Registering user account with information: {}", userDto);
    userService.register(userDto);
    return StandardRestResponse.getResponseEntity(null);
  }

  @PatchMapping(value = "/{username}")
  public ResponseEntity realNameVerification(
      @Valid RealNameVerificationDto realNameVerificationDto,
      @PathVariable("username") String username) {

    LOGGER.debug("Verifying user account with information: {}", realNameVerificationDto);
    if (!isCurrentUser(username))
      return StandardRestResponse.getResponseEntity(
          "Verification user target doesn't match", false, HttpStatus.FORBIDDEN);
    userService.realNameVerification(username, realNameVerificationDto);
    return StandardRestResponse.getResponseEntity(null);
  }

  public static boolean isCurrentUser(String username) {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return currentUser.getUsername().equals(username);
  }
}
