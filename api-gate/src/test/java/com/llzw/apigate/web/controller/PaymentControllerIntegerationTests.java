package com.llzw.apigate.web.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class PaymentControllerIntegerationTests extends ApiGateApplicationTests {

  MockMvc mvc;
  @Autowired
  UserRepository userRepository;
  User mockUser;
  @Autowired
  private WebApplicationContext context;

  @BeforeAll
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
    mockUser = userRepository.findByUsername("test_user_customer_username_0").get();
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void createTest() throws Exception {
    mvc.perform(
        post("/api/v1/payments")
            .param("orderId", "1")
            .param("subject", "Test Subject")
//            .with(user(userRepository.findByUsername("test_user_customer_username_0").get()))
    ).andExpect(status().isCreated());
  }
}
