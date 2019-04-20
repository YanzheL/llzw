package com.llzw.apigate.web.controller;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import com.llzw.apigate.persistence.dao.OrderRepository;
import com.llzw.apigate.persistence.dao.ProductRepository;
import com.llzw.apigate.persistence.dao.StockRepository;
import com.llzw.apigate.persistence.dao.UserRepository;
import com.llzw.apigate.persistence.entity.Order;
import com.llzw.apigate.spring.MockEntityFactory;
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
public class PaymentControllerIntegrationTests extends ApiGateApplicationTests {

  protected MockMvc mvc;

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected WebApplicationContext context;

  @Autowired
  protected OrderRepository orderRepository;

  @Autowired
  protected ProductRepository productRepository;

  @Autowired
  protected StockRepository stockRepository;

  @BeforeAll
  public void setup() throws Exception {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
    Order newOrder = MockEntityFactory.makeOrder(
        1L,
        stockRepository.findById(1L).get(),
        userRepository.findByUsername("test_user_customer_username_0").get()
    );
    orderRepository.save(newOrder);
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void createPaymentByCustomer() throws Exception {
    MvcResult result = mvc.perform(
        post("/api/v1/payments")
            .param("orderId", "1")
            .param("subject", "Test Subject")
            .param("description", "Test Description")
    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.orderString").isNotEmpty())
        .andReturn();
  }

  @Test
  public void createPaymentByNoUser() throws Exception {
    MvcResult result = mvc.perform(
        post("/api/v1/payments")
            .param("orderId", "1")
            .param("subject", "Test Subject")
            .param("description", "Test Description")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
        .andReturn();
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void createPaymentBySeller() throws Exception {
    MvcResult result = mvc.perform(
        post("/api/v1/payments")
            .param("orderId", "1")
            .param("subject", "Test Subject")
            .param("description", "Test Description")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
        .andReturn();
  }

  @Test
  public void retryNotExistPaymentByNoUser() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/payments/retry/200")
    )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andReturn();
  }
}