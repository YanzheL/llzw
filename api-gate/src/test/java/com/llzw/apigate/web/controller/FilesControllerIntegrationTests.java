package com.llzw.apigate.web.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.llzw.apigate.ApiGateApplicationTests;
import java.io.File;
import java.io.FileInputStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

@TestInstance(Lifecycle.PER_CLASS)
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FilesControllerIntegrationTests extends ApiGateApplicationTests {

  @BeforeAll
  public void setup() {
    mvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

  @WithUserDetails("test_user_seller_username_0")
  @RepeatedTest(5)
  @Order(1)
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

  @WithUserDetails("test_user_customer_username_0")
  @Test
  public void uploadPngByCustomer() throws Exception {
    File file = new File("../logo.png");
    MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
        "multipart/form-data",
        new FileInputStream(file));
    mvc.perform(multipart(apiBasePath + "/files").file(multipartFile))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @Test
  public void uploadPngByNoUser() throws Exception {
    File file = new File("../logo.png");
    MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
        "multipart/form-data",
        new FileInputStream(file));
    mvc.perform(multipart(apiBasePath + "/files").file(multipartFile))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  public void uploadOtherFileBySeller() throws Exception {
    File file = new File("src/main/resources/application-dev.yml");
    MockMultipartFile multipartFile = new MockMultipartFile("file", file.getName(),
        "multipart/form-data",
        new FileInputStream(file));
    mvc.perform(multipart(apiBasePath + "/files").file(multipartFile))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
    ;
  }

  @Test
  @Order(2)
  public void downloadPngByNoUser() throws Exception {
    mvc.perform(
        get(apiBasePath
            + "/files/b0339ffc5e42a813380a0da98b1a0fdf449195a430f497b041b80dfe98915e29"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
    ;
  }

  @WithUserDetails("test_user_seller_username_0")
  @Test
  @Order(3)
  public void deletePngBySeller() throws Exception {
    mvc.perform(
        delete(apiBasePath
            + "/files/b0339ffc5e42a813380a0da98b1a0fdf449195a430f497b041b80dfe98915e29"))
        .andDo(print())
        .andExpect(status().isOk())
    ;
    assertFalse(
        new File("storage/b0339ffc5e42a813380a0da98b1a0fdf449195a430f497b041b80dfe98915e29")
            .exists()
    );
  }
}
