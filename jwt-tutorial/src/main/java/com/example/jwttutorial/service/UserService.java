package com.example.jwttutorial.service;

import com.example.jwttutorial.dto.UserDto;
import com.example.jwttutorial.entity.Authority;
import com.example.jwttutorial.entity.User;
import com.example.jwttutorial.exception.DuplicateMemberException;
import com.example.jwttutorial.exception.NotFoundMemberException;
import com.example.jwttutorial.repository.UserRepository;
import com.example.jwttutorial.util.SecurityUtil;
import java.util.Collections;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  //회원가입 로직을 수행하는 메소드
  @Transactional
  public UserDto signup(UserDto userDto) {
    if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
      throw new DuplicateMemberException("이미 가입되어 있는 유저입니다.");
    }
    //회원가입 이력이 없는 유저의 권한을 만들어 준다.
    Authority authority = Authority.builder()
        .authorityName("ROLE_USER")
        .build();

    User user = User.builder()
        .username(userDto.getUsername())
        .password(passwordEncoder.encode(userDto.getPassword()))
        .nickname(userDto.getNickname())
        .authorities(Collections.singleton(authority))
        .activated(true)
        .build();
    return UserDto.from(userRepository.save(user));
  }

  //username에 해당하는 권한과 정보를 가져오는 메서드
  @Transactional(readOnly = true)
  public UserDto getUserWithAuthorities(String username) {
    return UserDto.from(userRepository.findOneWithAuthoritiesByUsername(username).orElse(null));
  }

  //SecurityContext에 저장된 username의 정보만 가져온다.
  @Transactional(readOnly = true)
  public UserDto getMyUserWithAuthorities() {
    return UserDto.from(
        SecurityUtil.getCurrentUsername()
            .flatMap(userRepository::findOneWithAuthoritiesByUsername)
            .orElseThrow(() -> new NotFoundMemberException("Member not found"))
    );
  }
}
