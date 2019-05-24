package com.llzw.apigate.spring;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Primary;

@Data
@ConfigurationProperties(prefix = "llzw.test")
// Without this may cause NoUniqueBeanDefinitionException
@Primary
public class TestDataProperties {

  public boolean loadUsers;

  public boolean loadProducts;

  public boolean loadStocks;

  public boolean loadAddresses;

}