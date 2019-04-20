package com.llzw.apigate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ApiGateApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiGateApplication.class, args);
  }
}
