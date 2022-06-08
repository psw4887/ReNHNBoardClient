package com.nhnacademy.nhn_board.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.nhn_board.dto.UserDTO;
import com.nhnacademy.nhn_board.entity.User;
import com.nhnacademy.nhn_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RestTemplate restTemplate;
    private final UserRepository uRepository;

    public boolean successLogin(String id, String pw) {

        return uRepository.existsByUserIdAndUserPw(id, pw);
    }

    public UserDTO findUserById(String id) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<UserDTO> exchange = restTemplate.exchange("http://localhost:9090/user/" + id,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return exchange.getBody();
    }
}
