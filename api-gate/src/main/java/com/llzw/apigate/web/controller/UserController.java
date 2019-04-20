package com.llzw.apigate.web.controller;

import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.UserService;
import com.llzw.apigate.web.dto.RealNameVerificationDto;
import com.llzw.apigate.web.dto.UpdatePasswordDto;
import com.llzw.apigate.web.dto.UserDto;
import com.llzw.apigate.web.util.RestResponseFactory;
import java.util.ArrayList;
import java.util.Collection;
import javax.validation.Valid;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// @RepositoryRestController
@RestController
@BasePathAwareController
@RequestMapping(value = "/users")
public class UserController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Setter(onMethod_ = @Autowired)
  private UserService userService;

  @PostMapping(value = "/register")
  public ResponseEntity register(@Valid UserDto userDto) {
    LOGGER.debug("Registering user account with information: {}", userDto);
    Collection<String> msgs = new ArrayList<>();
    userService.register(userDto, msgs);
    return RestResponseFactory.success(msgs);
  }

  @PutMapping(value = "/realNameVerification")
  public ResponseEntity realNameVerification(
      @Valid RealNameVerificationDto realNameVerificationDto) {
    LOGGER.debug("Verifying user account with information: {}", realNameVerificationDto);
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Collection<String> msgs = new ArrayList<>();
    userService.realNameVerification(currentUser.getUsername(), realNameVerificationDto, msgs);
    return RestResponseFactory.success(msgs);
  }

  @PutMapping(value = "/updatePassword")
  @PreAuthorize("hasAuthority('OP_MANAGE_PASSWORD')")
  public ResponseEntity updatePassword(@Valid UpdatePasswordDto updatePasswordDto) {

    LOGGER.debug("Verifying user account with information: {}", updatePasswordDto);
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    Collection<String> msgs = new ArrayList<>();
    userService.updateUserPassword(
        currentUser.getUsername(),
        updatePasswordDto.getOldPassword(),
        updatePasswordDto.getNewPassword(),
        msgs);
    return RestResponseFactory.success(msgs);
  }

  //  public static boolean isCurrentUser(String username) {
  //    User currentUser =
  //        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  //    return currentUser.getUsername().equals(username);
  //  }
}
