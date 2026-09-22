package org.example.project302.user.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserOauthService {
    private final UserRepository userRepository;

    // localId로 user 검색
    public User findByLocalId(String localId) throws IllegalArgumentException{
        User user = userRepository.findByLocalId(localId)
                .orElse(null);
        return user;
    }

}
