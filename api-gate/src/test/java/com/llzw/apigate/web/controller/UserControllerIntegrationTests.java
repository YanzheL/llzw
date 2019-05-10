package com.llzw.apigate.web.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTests extends ApiGateApplicationTests {

  @Autowired
  UserRepository userRepository;

  @BeforeAll
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @Test
  public void loginSeller() throws Exception {
    mvc.perform(
        post("/login")
            .param("username", "test_user_seller_username_0")
            .param("password", "test_user_seller_PASSWORD_0")
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

  @Test
  public void loginCustomer() throws Exception {
    mvc.perform(
        post("/login")
            .param("username", "test_user_customer_username_0")
            .param("password", "test_user_customer_PASSWORD_0")
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

  @Test
  public void registerValidCustomer() throws Exception {
    String username = "a12345";
    registerUser(
        username,
        "X_test_password_0",
        null,
        "test@test.org",
        "12345678",
        "ROLE_CUSTOMER"
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
    User user = userRepository.findByUsername(username).get();
    assertNotNull(user);
    assertNotNull(user.getCreatedAt());
    assertNotNull(user.getUpdatedAt());
  }

  @Test
  public void registerValidSeller() throws Exception {
    String username = "a12345";
    registerUser(
        username,
        "X_test_password_0",
        null,
        "test@test.org",
        "12345678",
        "ROLE_SELLER"
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
    User user = userRepository.findByUsername(username).get();
    assertNotNull(user);
    assertNotNull(user.getCreatedAt());
    assertNotNull(user.getUpdatedAt());
  }

  @Test
  public void registerInvalidRole() throws Exception {
    String username = "a12345";
    registerUser(
        username,
        "X_test_password_0",
        null,
        "test@test.org",
        "12345678",
        "ROLE_XXX"
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
    ;
    assertFalse(userRepository.findByUsername(username).isPresent());
  }

  @Test
  public void registerInvalidEmailAndRoleAndNickname() throws Exception {
    String username = "a12345";
    registerUser(
        username,
        "X_test_password_0",
        "asddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddasdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd",
        "test@testorg",
        "12345678",
        "ROLE_XXX"
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error.message").value(containsString("email")))
        .andExpect(jsonPath("$.error.message").value(containsString("role")))
        .andExpect(jsonPath("$.error.message").value(containsString("nickname")))
    ;
    assertFalse(userRepository.findByUsername(username).isPresent());
  }

  @Test
  public void getUserInfoByNoUser() throws Exception {
    mvc.perform(
        get(apiBasePath + "/users/me")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
    ;
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void getUserInfoBySeller() throws Exception {
    mvc.perform(
        get(apiBasePath + "/users/me")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.username").value("test_user_seller_username_0"))
    ;
  }

  private ResultActions registerUser(
      String username,
      String password,
      String nickname,
      String email,
      String phoneNumber, String role
  ) throws Exception {
    return mvc.perform(
        post(apiBasePath + "/users/register")
            .param("username", username)
            .param("password", password)
            .param("nickname", nickname)
            .param("email", email)
            .param("phoneNumber", phoneNumber)
            .param("role", role)
    );
  }
}
