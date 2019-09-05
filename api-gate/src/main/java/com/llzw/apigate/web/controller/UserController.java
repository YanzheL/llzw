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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// @RepositoryRestController is fucking not working as expected, referenced issue: https://jira.spring.io/browse/DATAREST-972
// @RestController creates duplicate endpoints with and without base-path. The same issue as described above.
@Validated
@Controller
@ResponseBody
@RequestMapping(value = "${spring.data.rest.base-path}/users")
@Transactional
public class UserController {

  private final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Setter(onMethod_ = @Autowired)
  private UserService userService;

  @PostMapping(value = "/register")
  public ResponseEntity register(@Valid @RequestBody UserDto userDto) throws RestApiException {
    LOGGER.debug("Registering user account with information: {}", userDto);
    return RestResponseEntityFactory.success(userService.register(userDto));
  }

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PutMapping(value = "/realNameVerification")
  public ResponseEntity realNameVerification(
      @Valid @RequestBody RealNameVerificationDto realNameVerificationDto,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    LOGGER.debug("Verifying user account with information: {}", realNameVerificationDto);
    return RestResponseEntityFactory.success(
        userService.realNameVerification(currentUser.getUsername(), realNameVerificationDto));
  }

  @PutMapping(value = "/updatePassword")
  @PreAuthorize("hasAuthority('OP_MANAGE_PASSWORD')")
  public ResponseEntity updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    LOGGER.debug("Verifying user account with information: {}", updatePasswordDto);
    return RestResponseEntityFactory.success(userService.updateUserPassword(
        currentUser.getUsername(),
        updatePasswordDto.getOldPassword(),
        updatePasswordDto.getNewPassword()
    ));
  }

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @GetMapping(value = "/me")
  public ResponseEntity getCurrentUserInfo(@AuthenticationPrincipal User currentUser) {
    return RestResponseEntityFactory.success(currentUser);
  }

  @PreAuthorize("hasAnyRole('SELLER','CUSTOMER')")
  @PatchMapping(value = "/me")
  public ResponseEntity updateCurrentUserInfo(
      @Valid @RequestBody UserPatchDto dto,
      @AuthenticationPrincipal User currentUser
  ) throws RestApiException {
    return RestResponseEntityFactory.success(
        userService.updateInfo(currentUser.getUsername(), dto)
    );
  }
}
