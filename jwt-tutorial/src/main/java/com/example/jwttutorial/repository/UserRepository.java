package com.example.jwttutorial.repository;

import com.example.jwttutorial.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  @EntityGraph(attributePaths = "authorities")
    // @EntityGraph: 해당 쿼리를 수행 될 때 Lazy 조회가 이고 Eager 조회로 authorities 정보를 같이 가져온다.
  Optional<User> findOneWithAuthoritiesByUsername(String username);
}
