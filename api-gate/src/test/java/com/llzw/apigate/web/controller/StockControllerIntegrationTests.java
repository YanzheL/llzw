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

@TestInstance(Lifecycle.PER_CLASS) //@BeforeAll和@AfterAll可以在测试接口中的静态方法上声明，也可以在接口默认方法上声明。
@Transactional
//@TestMethodOrder(MethodStocker.OrderAnnotation.class)
public class StockControllerIntegrationTests extends ApiGateApplicationTests {
  // 创建一个用于测试的mvc客户端，然后这个mvc客户端可以用来发起请求
  @BeforeAll
  public void setup() throws Exception {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  //@Order(1) //定义了组件的加载顺序
  public void createStock() throws Exception {

    MvcResult result = mvc.perform(
        post("/api/v1/stocks")
            .param("productId", "1")
            .param("producedAt", "2000-10-31T01:30:00.000-05:00")//new Date().toString())
            .param("shelfLife", "120")
            .param("totalQuantity", "50")
            .param("trackingId", "1234567")
            .param("carrierName", "运输公司")
    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andReturn();
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void getStock() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/stocks/1")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(1L))
        .andReturn();
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void searchStockByProductand() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/stocks")
            .param("productId", "1")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").isNotEmpty())
        .andReturn();
  }


}
