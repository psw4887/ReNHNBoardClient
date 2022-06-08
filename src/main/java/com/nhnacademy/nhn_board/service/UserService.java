package com.nhnacademy.nhn_board.service;

import com.nhnacademy.nhn_board.entity.User;
import com.nhnacademy.nhn_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository uRepository;

    public boolean successLogin(String id, String pw) {

        return uRepository.existsByUserIdAndUserPw(id, pw);
    }

    public User findUserById(String id) {

        return uRepository.findByUserId(id).orElse(null);
    }
}
