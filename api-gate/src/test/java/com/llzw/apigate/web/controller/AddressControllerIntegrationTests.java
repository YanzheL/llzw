package com.llzw.apigate.web.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class AddressControllerIntegrationTests extends ApiGateApplicationTests {

  @BeforeAll
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void createAddress() throws Exception {
    MvcResult result = mvc.perform(
        post("/api/v1/addresses")
            .param("province", "Guangdong")
            .param("city", "Shenzhen")
            .param("district", "Futian")
            .param("address", "Yi Tian Road")
            .param("zip", "123456")
    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andReturn();
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void getMyAddresses() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/addresses")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").isNotEmpty())
        .andReturn();
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void getSpecificAddressByOwner() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/addresses/1")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1L))
        .andReturn();
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void getSpecificAddressByOther() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/addresses/1")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
        .andReturn();
  }


}
