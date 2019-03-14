package com.llzw.apigate.web.controller;

import com.llzw.apigate.service.UserService;
import com.llzw.apigate.web.dto.UserDto;
import com.llzw.apigate.web.util.StandardRestResponse;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
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
}
