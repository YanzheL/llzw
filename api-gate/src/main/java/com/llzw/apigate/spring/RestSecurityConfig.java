package com.llzw.apigate.spring;

import com.llzw.apigate.security.CustomAuthenticationProvider;
import com.llzw.apigate.security.RestAuthenticationEntryPoint;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

  @Setter(onMethod_ = @Autowired)
  private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  @Setter(onMethod_ = @Autowired)
  private AuthenticationSuccessHandler successHandler;

  @Setter(onMethod_ = @Autowired)
  private UserDetailsService userDetailsService;

  private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();

  @Autowired
  public RestSecurityConfig() {
    super();
    SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authProvider());
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeRequests()
        .and()
        .exceptionHandling()
        //                .accessDeniedHandler(accessDeniedHandler)
        .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
        .authorizeRequests()
//        .antMatchers("/login*", "/logout*", "/api/v1/users/register", "/api/v1/product/**")
//        .permitAll()
        .and()
        .formLogin()
        .loginPage("/login") // 设
        .successHandler(successHandler)
        .failureHandler(failureHandler)
        //        .authenticationDetailsSource(authenticationDetailsSource)
        .permitAll()
        .and()
        .sessionManagement()
        .maximumSessions(1)
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
  public DaoAuthenticationProvider authProvider() {
    final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(encoder());
    return authProvider;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }
}
