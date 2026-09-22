package org.example.project302.user.repository;

import org.example.project302.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLocalId(String localId);

  User findByEmail(String email);

  Optional<User> findByToken(String refreshToken);

  boolean existsByLocalId(String localId);

  boolean existsByNickname(String nickname);

}
