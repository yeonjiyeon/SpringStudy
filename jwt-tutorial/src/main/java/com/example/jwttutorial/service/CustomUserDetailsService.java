package com.example.jwttutorial.service;

import com.example.jwttutorial.entity.User;
import com.example.jwttutorial.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  //로그인시 DB에서 유저 정보와 권한 정보를 가져온다.
  @Override
  @Transactional
  public UserDetails loadUserByUsername(final String username) {
    return userRepository.findOneWithAuthoritiesByUsername(username)
        .map(user -> createUser(username, user))
        .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
  }

  private org.springframework.security.core.userdetails.User createUser(String username, User user) {
    if (!user.isActivated()) {
      throw new RuntimeException(username + " -> 활성화되어 있지 않습니다.");
    }

    List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
        .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
        .collect(Collectors.toList());

    return new org.springframework.security.core.userdetails.User(user.getUsername(),
        user.getPassword(),
        grantedAuthorities);// //로그인시 DB에서 유저 정보와 권한 정보를 가져온다. 해당 정보를 기반으로 userdetails.User 객체를 생성해서 리턴
  }
}
