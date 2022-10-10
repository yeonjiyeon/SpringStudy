package com.example.jwttutorial.controller;

import com.example.jwttutorial.dto.UserDto;
import com.example.jwttutorial.service.UserService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/hello")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("hello");
  }

  @PostMapping("/test-redirect")
  public void testRedirect(HttpServletResponse response) throws IOException {
    response.sendRedirect("/api/user");
  }

  //userDto를 파라미터로 받아서 userService의 signup메소드 호출
  @PostMapping("/signup")
  public ResponseEntity<UserDto> signup(
      @Valid @RequestBody UserDto userDto
  ) {
    return ResponseEntity.ok(userService.signup(userDto));
  }

  //@PreAuthorize를 통해서 USER, ADMIN 두가지 권한 모두 허용
  @GetMapping("/user")
  @PreAuthorize("hasAnyRole('USER','ADMIN')")
  public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request) {
    return ResponseEntity.ok(userService.getMyUserWithAuthorities());
  }

  //ADMIN 권한만 호출할 수 있도록 설정
  @GetMapping("/user/{username}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<UserDto> getUserInfo(@PathVariable String username) {
    return ResponseEntity.ok(userService.getUserWithAuthorities(username));
  }
}
