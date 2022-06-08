package com.nhnacademy.nhn_board.service;

import com.nhnacademy.nhn_board.entity.Like;
import com.nhnacademy.nhn_board.entity.Post;
import com.nhnacademy.nhn_board.entity.User;
import com.nhnacademy.nhn_board.repository.LikeRepository;
import com.nhnacademy.nhn_board.repository.PostRepository;
import com.nhnacademy.nhn_board.repository.UserRepository;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository pRepository;
    private final UserRepository uRepository;
    private final LikeRepository lRepository;

    public boolean isLike(Integer postNo, Integer userNo) {

        return Objects.nonNull(lRepository.findLike(postNo, userNo));
    }

    public void insertLike(Integer postNo, Integer userNo) {

        Post post = pRepository.findById(postNo).orElse(null);
        User user = uRepository.findById(userNo).orElse(null);

        Like like = new Like(post, user);

        lRepository.save(like);
    }

    @Transactional
    public void deleteLike(Integer postNo, Integer userNo) {
        Like like = lRepository.findLike(postNo, userNo);

        lRepository.deleteById(like.getLikeNo());
    }
}
