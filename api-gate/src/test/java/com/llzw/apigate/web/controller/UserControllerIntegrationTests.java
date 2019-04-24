package com.llzw.apigate.web.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerIntegrationTests extends ApiGateApplicationTests {

  @Autowired
  UserRepository userRepository;

  // 创建一个用于测试的mvc客户端，然后这个mvc客户端可以用来发起请求
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
            .param("password", "test_user_seller_password_0")
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
            .param("password", "test_user_customer_password_0")
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
  }

  @Test
  public void registerSeller() throws Exception {
    mvc.perform(
        post(apiBasePath + "/users/register")
            .param("username", "X_user_seller_username_0")
            .param("password", "X_user_seller_password_0")
            .param("nickname", "fff")
            .param("email", "test1@test.org")
            .param("phoneNumber", "12345678")
            .param("role", "ROLE_SELLER")
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
    User user = userRepository.findByUsername("X_user_seller_username_0").get();
    assertNotNull(user);
    assertNotNull(user.getCreatedAt());
    assertNotNull(user.getUpdatedAt());
  }

  @Test
  public void registerCustomer() throws Exception {
    String username = "12345";
    mvc.perform(
        post(apiBasePath + "/users/register")
            .param("username", username)
            .param("password", "X_user_customer_password_0")
            .param("nickname", "fff")
            .param("email", "test2@test.org")
            .param("phoneNumber", "12345678")
            .param("role", "ROLE_SELLER")
    )
        .andDo(print())
        .andExpect(status().isOk())
    ;
    User user = userRepository.findByUsername(username).get();
    assertNotNull(user);
    assertNotNull(user.getCreatedAt());
    assertNotNull(user.getUpdatedAt());
  }
}
