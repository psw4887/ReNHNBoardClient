package com.nhnacademy.nhn_board.service;

import com.nhnacademy.nhn_board.dto.UserDTO;
import com.nhnacademy.nhn_board.entity.Comment;
import com.nhnacademy.nhn_board.entity.Post;
import com.nhnacademy.nhn_board.entity.User;
import com.nhnacademy.nhn_board.repository.CommentRepository;
import com.nhnacademy.nhn_board.repository.PostRepository;
import com.nhnacademy.nhn_board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostService pService;
    private final CommentRepository cRepository;
    private final UserService uService;

    public Comment getComment(Integer commentNo) {

        return cRepository.findById(commentNo).orElse(null);
    }

    @Transactional
    public void registerComment(Integer postNo, String content, HttpServletRequest req) {

        Post post = pService.getPost(postNo);
        UserDTO userDTO = uService.findUserById(String.valueOf(req.getSession(false).getAttribute("id")));

        User user = new User(userDTO.getUserNo(), userDTO.getUserId(), userDTO.getUserPw(), userDTO.getCheckAdmin());
        Comment comment = new Comment(post, user , content);

        cRepository.save(comment);
    }

    @Transactional
    public void modifyComment(Integer commentNo, String content) {

        Comment comment = cRepository.findById(commentNo).orElse(null);

        comment.setContent(content);

        cRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Integer commentNo) {

        cRepository.deleteById(commentNo);
    }
}
