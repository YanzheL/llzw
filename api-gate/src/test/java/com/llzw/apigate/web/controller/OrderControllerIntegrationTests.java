package com.llzw.apigate.web.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
  @Order(1)
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

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void createOrderWithNonExistProductIdByCustomer() throws Exception {
    MvcResult result = mvc.perform(
        post("/api/v1/orders")
            .param("productId", "100")
            .param("quantity", "10")
            .param("addressId", "1")
    )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andReturn();
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  @Order(2)
  public void getOrderByCustomer() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/orders/1")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1L))
        .andReturn();
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void getNonExistOrderByCustomer() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/orders/100")
    )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andReturn();
  }

  @Test
  public void getNonExistOrderByNoUser() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/orders/100")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error.type").isNotEmpty())
        .andReturn();
  }
}
