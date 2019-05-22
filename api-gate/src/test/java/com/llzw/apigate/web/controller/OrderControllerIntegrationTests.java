package com.llzw.apigate.web.controller;

import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import com.llzw.apigate.message.RestApiResponse;
import com.llzw.apigate.web.dto.OrderCreateDto;
import com.llzw.apigate.web.dto.OrderShipDto;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@Commit
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIntegrationTests extends ApiGateApplicationTests {

  protected UUID testUUID = UUID.randomUUID();

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
  @Transactional
  @SuppressWarnings("unchecked")
  public void createOrderByCustomer() throws Exception {
    OrderCreateDto dto = new OrderCreateDto();
    dto.setProductId(1L);
    dto.setQuantity(431);
    dto.setAddressId(11L);
    dto.setRemark("Test");
    MvcResult result = mvc.perform(
        post(apiBasePath + "/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.stocks.length()").value(5))
        .andReturn();
    RestApiResponse response = objectMapper
        .readValue(result.getResponse().getContentAsString(), RestApiResponse.class);
    Map<String, String> order = (Map<String, String>) response.getData();
    testUUID = UUID.fromString(order.get("id"));
    result = mvc.perform(
        get(apiBasePath + "/products/1")
    )
        .andDo(print())
        .andExpect(jsonPath("$.data.stat.salesLastMonth").value(greaterThan(0)))
        .andReturn()
    ;
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void createOrderWithNonExistProductIdByCustomer() throws Exception {
    OrderCreateDto dto = new OrderCreateDto();
    dto.setProductId(1000L);
    dto.setQuantity(10);
    dto.setAddressId(1L);
    dto.setRemark("Test");
    mvc.perform(
        post(apiBasePath + "/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
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
        get(apiBasePath + "/orders/" + testUUID.toString())
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(testUUID.toString()))
    ;
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  @Order(2)
  public void searchOrdersByCustomer() throws Exception {
    mvc.perform(
        get(apiBasePath + "/orders")
            .param("stockId", "12")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data[0].id").value(testUUID.toString()))
    ;
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void getNonExistOrderByCustomer() throws Exception {
    mvc.perform(
        get(apiBasePath + "/orders/c7537a95-1aae-4dfe-a97f-1a41241da367")
    )
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @Test
  public void getNonExistOrderByNoUser() throws Exception {
    mvc.perform(
        get(apiBasePath + "/orders/c7537a95-1aae-4dfe-a97f-1a41241da367")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.error.type").isNotEmpty())
    ;
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void deliveryConfirmByCustomer() throws Exception {
    mvc.perform(
        patch(apiBasePath + "/orders/" + testUUID.toString() + "/DELIVERY_CONFIRM")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(testUUID.toString()))
    ;
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void deliveryConfirmBySeller() throws Exception {
    mvc.perform(
        patch(apiBasePath + "/orders/" + testUUID.toString() + "/DELIVERY_CONFIRM")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @Test
  public void deliveryConfirmByNoUser() throws Exception {
    mvc.perform(
        patch(apiBasePath + "/orders/" + testUUID.toString() + "/DELIVERY_CONFIRM")
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void PatchOrderBySeller() throws Exception {
    OrderShipDto dto = new OrderShipDto();
    dto.setCarrierName("SF-Express");
    dto.setShippingTime(new Date());
    dto.setTrackingId("12345678");
    mvc.perform(
        patch(apiBasePath + "/orders/" + testUUID.toString() + "/SHIP")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
    ;
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void PatchOrderBySellerWithPartialInfo() throws Exception {
    OrderShipDto dto = new OrderShipDto();
    dto.setCarrierName("SF-Express");
    dto.setShippingTime(new Date());
    mvc.perform(
        patch(apiBasePath + "/orders/" + testUUID.toString() + "/SHIP")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void PatchOrderByCustomer() throws Exception {
    OrderShipDto dto = new OrderShipDto();
    dto.setCarrierName("SF-Express");
    dto.setShippingTime(new Date());
    dto.setTrackingId("12345678");
    mvc.perform(
        patch(apiBasePath + "/orders/" + testUUID.toString() + "/SHIP")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto))
    )
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }
}
