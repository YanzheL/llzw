package com.llzw.apigate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableConfigurationProperties
public class ApiGateApplicationTests {

  @Autowired
  protected WebApplicationContext context;

  @Autowired
  protected ObjectMapper objectMapper;

  protected MockMvc mvc;

  @Value("${spring.data.rest.base-path}")
  protected String apiBasePath;

  @Test
  public void contextLoads() {
  }
}
