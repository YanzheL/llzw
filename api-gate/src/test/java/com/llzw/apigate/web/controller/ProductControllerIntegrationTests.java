package com.llzw.apigate.web.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import com.llzw.apigate.message.RestApiResponse;
import com.llzw.apigate.service.FileStorageService;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS) //@BeforeAll和@AfterAll可以在测试接口中的静态方法上声明，也可以在接口默认方法上声明。
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Commit
public class ProductControllerIntegrationTests extends ApiGateApplicationTests {

  protected String createdMainImageFile;
  protected Long createdProductId;

  @Autowired
  FileStorageService fileStorageService;

  // 创建一个用于测试的mvc客户端，然后这个mvc客户端可以用来发起请求
  @BeforeAll
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @AfterAll
  public void clean() {
    fileStorageService.delete("b0339ffc5e42a813380a0da98b1a0fdf449195a430f497b041b80dfe98915e29");
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  @Order(1)
  @SuppressWarnings("unchecked")
  @Transactional
  public void createProduct() throws Exception {
    uploadPngBySeller();

    MvcResult result = mvc.perform(
        post("/api/v1/products")
            .param("name", "auto test 1")
            .param("introduction", "Hello world")//new Date().toString())
            .param("mainImageFiles",
                "b0339ffc5e42a813380a0da98b1a0fdf449195a430f497b041b80dfe98915e29")
            .param("price", "1234.56")
            .param("ca", "321532135")
            .param("caId", "1111")
            .param("caFile", "b0339ffc5e42a813380a0da98b1a0fdf449195a430f497b041b80dfe98915e29")
    )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andReturn();
    RestApiResponse response = objectMapper
        .readValue(result.getResponse().getContentAsString(), RestApiResponse.class);
    Map<String, Object> data = (Map<String, Object>) response.getData();
    createdMainImageFile = ((List<String>) (data.get("mainImageFiles"))).get(0);
    createdProductId = Long.valueOf((Integer) data.get("id"));
  }

  @Test
  @Order(2)
  public void getProducts() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/products")
            .param("global", "Macbook 4")
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data").isArray())
        .andReturn();
  }

  @Test
  @Order(2)
  public void getProduct() throws Exception {
    MvcResult result = mvc.perform(
        get("/api/v1/products/" + createdProductId)
    )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.id").value(createdProductId))
        .andExpect(jsonPath("$.data.mainImageFiles[0]").value(createdMainImageFile))
        .andReturn();
  }

  public void uploadPngBySeller() throws Exception {
    File file = new File("../logo.png");
    MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
        "multipart/form-data",
        new FileInputStream(file));
    mvc.perform(multipart(apiBasePath + "/files").file(multipartFile))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.hash").isNotEmpty())
        .andExpect(jsonPath("$.data.mimeType").value("image/png"))
    ;
    assertTrue(
        new File("storage/b0339ffc5e42a813380a0da98b1a0fdf449195a430f497b041b80dfe98915e29")
            .exists()
    );
  }

}
