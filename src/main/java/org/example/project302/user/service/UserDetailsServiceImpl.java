package org.example.project302.user.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.user.dto.CustomSecurityUserDetails;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String localId) throws UsernameNotFoundException {
        User result = userService.findByLocalId(localId); // localId로 User 받아옴

        if (result != null) {

            return new CustomSecurityUserDetails(result);
        }
        return null;

    }
}