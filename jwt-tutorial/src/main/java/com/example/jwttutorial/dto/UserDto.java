package com.example.jwttutorial.dto;

import com.example.jwttutorial.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//회원 가입에 사용할 dto
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  @NotNull
  @Size(min = 3, max = 50)
  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotNull
  @Size(min = 3, max = 100)
  private String password;

  @NotNull
  @Size(min = 3, max = 50)
  private String nickname;

  private Set<AuthorityDto> authorityDtoSet;

  public static UserDto from(User user) {
    if(user == null) return null;

    return UserDto.builder()
        .username(user.getUsername())
        .nickname(user.getNickname())
        .authorityDtoSet(user.getAuthorities().stream()
            .map(authority -> AuthorityDto.builder().authorityName(authority.getAuthorityName()).build())
            .collect(Collectors.toSet()))
        .build();
  }
}