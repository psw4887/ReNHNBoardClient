package com.nhnacademy.nhn_board.service;

import com.nhnacademy.nhn_board.dto.OnlyTitleContentDTO;
import com.nhnacademy.nhn_board.dto.PostContentDTO;
import com.nhnacademy.nhn_board.dto.UserDTO;
import com.nhnacademy.nhn_board.dto.ViewPostDTO;
import com.nhnacademy.nhn_board.dto.complete.ContentDTO;
import com.nhnacademy.nhn_board.dto.complete.PostListDTO;
import com.nhnacademy.nhn_board.entity.Comment;
import com.nhnacademy.nhn_board.entity.Post;
import com.nhnacademy.nhn_board.entity.User;
import com.nhnacademy.nhn_board.entity.View;
import com.nhnacademy.nhn_board.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final RestTemplate restTemplate;
    private final PostRepository pRepository;
    private final UserService uService;
    private final ViewRepository vRepository;
    private final LikeRepository lRepository;

    public List<PostListDTO> getPageablePostList(Integer page) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<PostListDTO>> exchange = restTemplate.exchange("http://localhost:9090/board/view/" + page,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return exchange.getBody();
    }

    public List<PostListDTO> getPageableRecoverPostList(Integer page) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<PostListDTO>> exchange = restTemplate.exchange("http://localhost:9090/board/recover/" + page,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return exchange.getBody();
    }

    public List<PostListDTO> getPageableSearchPostList(Integer page, String title) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<List<PostListDTO>> exchange = restTemplate.exchange(
                "http://localhost:9090/board/search/" + title + "/" + page,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return exchange.getBody();
    }

    public ContentDTO getContentByPostNo(Integer postNo) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<ContentDTO> exchange = restTemplate.exchange("http://localhost:9090/board/content/" + postNo,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        return exchange.getBody();
    }

    public OnlyTitleContentDTO getOnlyTitleContent(Integer postNo) {

        return pRepository.getOnlyModify(postNo);
    }

    @Transactional
    public void postRegister(String title, String content, HttpServletRequest req) {

        UserDTO userdto = uService.findUserById((String) req.getSession(false).getAttribute("id"));
        User user = new User(userdto.getUserNo(), userdto.getUserId(), userdto.getUserPw(), userdto.getCheckAdmin());
        Post post = new Post(user, title, content, LocalDateTime.now(), null, false);

        pRepository.save(post);
    }

    @Transactional
    public void postModify(String title, String content, Integer postNo) {

        Post post = pRepository.findById(postNo).orElse(null);
        post.setTitle(title);
        post.setContent(content);
        post.setModifyDateTime(LocalDateTime.now());

        pRepository.save(post);
    }

    @Transactional
    public void postDelete(Integer postNo) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        restTemplate.exchange("http://localhost:9090/board/delete/" + postNo,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
    }

    @Transactional
    public void postRecover(Integer postNo) {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);
        restTemplate.exchange("http://localhost:9090/board/recover/" + postNo,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
    }

    @Transactional
    public void insertView(Integer postNo, HttpServletRequest req) {
        UserDTO userdto = uService.findUserById((String) req.getSession(false).getAttribute("id"));
        User user = new User(userdto.getUserNo(), userdto.getUserId(), userdto.getUserPw(), userdto.getCheckAdmin());
        Post post = pRepository.findById(postNo).orElse(null);

        View.ViewPK viewPK = new View.ViewPK(postNo, user.getUserNo());

        if(vRepository.existsById(viewPK)) {
            return;
        }

        View view = new View(viewPK, post, user);

        vRepository.save(view);
    }
}
