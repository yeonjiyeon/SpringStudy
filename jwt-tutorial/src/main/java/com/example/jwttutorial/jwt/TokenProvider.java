package com.example.jwttutorial.jwt;

import org.springframework.security.core.userdetails.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
//Token Provider : 토큰의 생성, 토큰의 유효성 검증 등을 담당
public class TokenProvider implements InitializingBean {

  private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
  private static final String AUTHORITIES_KEY = "auth";
  private final String secret;
  private final long tokenValidityInMilliseconds;
  private Key key;

  public TokenProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSeconds) {
    this.secret = secret;
    this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
  }


  //빈이 생성 되고 의존성 주읩을 받은 후에 secret 값을 Base64 Decode 해서 key 변수에 할당당
 @Override
  public void afterPropertiesSet() {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String createToken(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));// 권한들 가져오기

    long now = (new Date()).getTime();
    Date validity = new Date(now + this.tokenValidityInMilliseconds);//만료시간 설정정
    return Jwts.builder()
        .setSubject(authentication.getName())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(key, SignatureAlgorithm.HS512)
        .setExpiration(validity)
        .compact();// 토큰 생성
  }

  //Token에 담겨있는 정보를 이용해 Authentication 객체를 리턴하는 메소드 생성
  public Authentication getAuthentication(String token) {
    Claims claims = Jwts // 토큰으로 클레임(Json map)을 만들고
        .parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());//클레임에서 권한 정보를 빼내서

    User principal = new User(claims.getSubject(), "", authorities);//유저 객체를 만든다

    return new UsernamePasswordAuthenticationToken(principal, token, authorities);
  }

  //토큰을 파라미터로 받아서 토큰의 유효성 검사를 할 수 있는 메서드
  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      logger.info("잘못된 JWT 서명입니다.");
    } catch (ExpiredJwtException e) {
      logger.info("만료된 JWT 토큰입니다.");
    } catch (UnsupportedJwtException e) {
      logger.info("지원되지 않는 JWT 토큰입니다.");
    } catch (IllegalArgumentException e) {
      logger.info("JWT 토큰이 잘못되었습니다.");
    }
    return false;
  }
}
