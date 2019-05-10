package com.llzw.apigate.web.controller;

import com.llzw.apigate.message.RestResponseEntityFactory;
import com.llzw.apigate.message.error.RestApiException;
import com.llzw.apigate.persistence.entity.User;
import com.llzw.apigate.service.UserService;
import com.llzw.apigate.web.dto.RealNameVerificationDto;
import com.llzw.apigate.web.dto.UpdatePasswordDto;
import com.llzw.apigate.web.dto.UserDto;
import com.llzw.apigate.web.dto.UserPatchDto;
import javax.validation.Valid;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@BasePathAwareController
@RequestMapping(value = "/users")
@Transactional
public class UserController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Setter(onMethod_ = @Autowired)
  private UserService userService;

  @PostMapping(value = "/register")
  public ResponseEntity register(@Valid UserDto userDto) throws RestApiException {
    LOGGER.debug("Registering user account with information: {}", userDto);
    return RestResponseEntityFactory.success(userService.register(userDto));
  }

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PutMapping(value = "/realNameVerification")
  public ResponseEntity realNameVerification(
      @Valid RealNameVerificationDto realNameVerificationDto) throws RestApiException {
    LOGGER.debug("Verifying user account with information: {}", realNameVerificationDto);
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(
        userService.realNameVerification(currentUser.getUsername(), realNameVerificationDto));
  }

  @PutMapping(value = "/updatePassword")
  @PreAuthorize("hasAuthority('OP_MANAGE_PASSWORD')")
  public ResponseEntity updatePassword(@Valid UpdatePasswordDto updatePasswordDto)
      throws RestApiException {
    LOGGER.debug("Verifying user account with information: {}", updatePasswordDto);
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(userService.updateUserPassword(
        currentUser.getUsername(),
        updatePasswordDto.getOldPassword(),
        updatePasswordDto.getNewPassword()
    ));
  }

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @GetMapping(value = "/me")
  public ResponseEntity getCurrentUserInfo() {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(
        currentUser
    );
  }

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PatchMapping(value = "/me")
  public ResponseEntity updateCurrentUserInfo(@Valid UserPatchDto dto) throws RestApiException {
    User currentUser =
        ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    return RestResponseEntityFactory.success(
        userService.updateInfo(currentUser.getUsername(), dto)
    );
  }
}
