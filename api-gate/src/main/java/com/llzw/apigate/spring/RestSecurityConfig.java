package com.llzw.apigate.spring;

import com.llzw.apigate.security.RestAuthenticationEntryPoint;
import java.util.Arrays;
import java.util.Collections;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

  @Setter(onMethod_ = @Autowired)
  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Setter(onMethod_ = @Autowired)
  private AuthenticationSuccessHandler successHandler;

  private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

  @Value("${spring.data.rest.base-path}")
  protected String apiBasePath;

  @Autowired
  public RestSecurityConfig() {
    super();
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "*"));
    configuration.setAllowedMethods(Collections.singletonList("*"));
    configuration.setAllowedHeaders(Collections.singletonList("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.cors()
        .and()
        .csrf()
        .disable()
//        .authorizeRequests()
//        .and()
        .exceptionHandling()
        //                .accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
        .authorizeRequests()
//        .antMatchers("/login*", "/logout*", "/api/v1/users/register", "/api/v1/product/**")
//        .permitAll()
        .and()
        .formLogin()
        .loginPage(apiBasePath + "/login") // 设
        .successHandler(successHandler)
        .failureHandler(failureHandler)
        //        .authenticationDetailsSource(authenticationDetailsSource)
        .permitAll()
        .and()
        .sessionManagement()
        .maximumSessions(10)
        .sessionRegistry(sessionRegistry())
        .and()
        .sessionFixation()
        .none();
  }

  @Bean
  public PasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}
