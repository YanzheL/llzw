package com.llzw.apigate.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class RestWebConfig implements WebMvcConfigurer {

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
        .defaultContentType(MediaType.APPLICATION_JSON_UTF8)
        .favorPathExtension(false);
  }

  @Override
  public void configurePathMatch(PathMatchConfigurer matcher) {
    matcher.setUseSuffixPatternMatch(false);
  }

}
