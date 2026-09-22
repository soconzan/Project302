package org.example.project302.user.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final UserRepository repository;

    public User findUser(String userId) throws IllegalArgumentException{
        return repository.findById(Long.valueOf(userId))
                .orElseThrow(()-> new IllegalArgumentException("Invalid user"));
    }

    // 유저에 refresh 토큰 저장
    public User saveToken(User user) throws IllegalArgumentException{

        return repository.save(user);

    }
    // 토큰 갱신 or 삽입
    public User save(User refreshToken) {
        return repository.save(refreshToken);
    }
}
