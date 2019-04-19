package com.llzw.apigate;

import com.llzw.apigate.spring.AlipayProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@RunWith(SpringRunner.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableConfigurationProperties({AlipayProperties.class})
public class ApiGateApplicationTests {

  @Test
  public void contextLoads() {
  }
}
