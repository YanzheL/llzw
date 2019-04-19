package com.llzw.apigate.web.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class OrderControllerIntegrationTests extends ApiGateApplicationTests {

  protected MockMvc mvc;

  @Autowired
  protected WebApplicationContext context;

  @BeforeAll
  public void setup() throws Exception {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void createOrderByCustomer() throws Exception {
    MvcResult result = mvc.perform(
        post("/api/v1/orders")
            .param("productId", "1")
            .param("quantity", "10")
            .param("addressId", "1")
    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andReturn();
  }
}
