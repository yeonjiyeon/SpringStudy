package com.example.jwttutorial.jwt;

import com.example.jwttutorial.jwt.JwtFilter;
import com.example.jwttutorial.jwt.TokenProvider;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//tokenProvide, JwtFilter를 SecurityConfig에 적용할 때 사용 할 클래스
public class JwtSecurityConfig extends
    SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
  private TokenProvider tokenProvider;
  public JwtSecurityConfig(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  
  @Override
  public void configure(HttpSecurity http) {
    http.addFilterBefore(
        new JwtFilter(tokenProvider),
        UsernamePasswordAuthenticationFilter.class
    );
  }
}