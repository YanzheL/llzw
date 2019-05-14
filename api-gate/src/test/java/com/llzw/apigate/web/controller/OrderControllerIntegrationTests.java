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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIntegrationTests extends ApiGateApplicationTests {

  // 创建一个用于测试的mvc客户端，然后这个mvc客户端可以用来发起请求
  @BeforeAll
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  @Order(1)
  public void createOrderByCustomer() throws Exception {
    mvc.perform(
        post(apiBasePath + "/orders")
            .param("productId", "1")
            .param("quantity", "10")
            .param("addressId", "11")
    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
    ;
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void createOrderWithNonExistProductIdByCustomer() throws Exception {
    mvc.perform(
        post(apiBasePath + "/orders")
            .param("productId", "100")
            .param("quantity", "10")
            .param("addressId", "1")
    )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  @Order(2)
  public void getOrderByCustomer() throws Exception {
    mvc.perform(
        get(apiBasePath + "/orders/1")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1L))
    ;
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void getNonExistOrderByCustomer() throws Exception {
    mvc.perform(
        get(apiBasePath + "/orders/100")
    )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @Test
  public void getNonExistOrderByNoUser() throws Exception {
    mvc.perform(
        get(apiBasePath + "/orders/100")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error.type").isNotEmpty())
    ;
  }
}
