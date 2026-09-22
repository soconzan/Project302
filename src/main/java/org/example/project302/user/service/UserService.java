package org.example.project302.user.service;

import lombok.RequiredArgsConstructor;
import org.example.project302.user.dto.AddUserRequest;
import org.example.project302.user.dto.AddUserResponse;
import org.example.project302.user.entity.User;
import org.example.project302.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;

    private User requestToEntity(AddUserRequest dto) { // 비밀번호 암호화
        String password = bCryptPasswordEncoder.encode(dto.getPassword());
        dto.setPassword(password);
        return new User(dto);
    }

    private AddUserResponse entityToResponse(User user) {
        return new AddUserResponse(user.getUserId(),"ok");
    }

    public AddUserResponse addUser(AddUserRequest dto) { // 새로운 유저 생성
        User result = userRepository.save(requestToEntity(dto)); // 비밀번호 암호화 -> 저장
        return entityToResponse(result);
    }

    // userId로 user 검색
    public User findById(String userId) throws IllegalArgumentException{
        return userRepository.findById(Long.valueOf(userId))
                .orElseThrow(()-> new IllegalArgumentException("Invalid user"));
    }

    // localId로 user 검색
    public User findByLocalId(String localId) throws IllegalArgumentException{
        User user = userRepository.findByLocalId(localId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원 없음 : " + localId));
        return user;
    }


    // 가입된 회원 확인 - email
    public User findByEmail(String email)throws IllegalArgumentException{
        return userRepository.findByEmail(email);
    }

    // localId 중복 체크
    public boolean hasUserId(String localId) {
        return userRepository.existsByLocalId(localId);
    }

    // nickname 중복 체크
    public  boolean hasUserNickname(String nickname){return userRepository.existsByNickname(nickname);}

    public User passwordUpdate(String localId, String password) {
        User user = findByLocalId(localId);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(user);
    }

    public boolean passwordCheck(String localId,String password){
        User user = findByLocalId(localId);
        System.out.println(Objects.equals(user.getPassword(), bCryptPasswordEncoder.encode(password)));
        return Objects.equals(user.getPassword(), bCryptPasswordEncoder.encode(password));
    }


    // 대학명, 주소 저장


}
