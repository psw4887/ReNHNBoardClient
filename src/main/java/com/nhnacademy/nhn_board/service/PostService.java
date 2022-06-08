package com.nhnacademy.nhn_board.service;

import com.nhnacademy.nhn_board.dto.OnlyTitleContentDTO;
import com.nhnacademy.nhn_board.dto.PostContentDTO;
import com.nhnacademy.nhn_board.dto.ViewPostDTO;
import com.nhnacademy.nhn_board.dto.complete.ContentDTO;
import com.nhnacademy.nhn_board.dto.complete.PostListDTO;
import com.nhnacademy.nhn_board.entity.Comment;
import com.nhnacademy.nhn_board.entity.Post;
import com.nhnacademy.nhn_board.entity.User;
import com.nhnacademy.nhn_board.entity.View;
import com.nhnacademy.nhn_board.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository pRepository;
    private final UserRepository uRepository;
    private final ViewRepository vRepository;
    private final CommentRepository cRepository;
    private final LikeRepository lRepository;

    public List<PostListDTO> getPageablePostList(Pageable pageable, HttpServletRequest req) {

        User user = new User();
        boolean isGuest = true;

        if(Objects.nonNull(req.getSession(false))) {
            user = uRepository.findByUserId((String) req.getSession(false).getAttribute("id")).orElse(null);
            isGuest = false;
        }
            List<ViewPostDTO> viewPostDTOS = pRepository.getPageableList(pageable).getContent();
            List<PostListDTO> postListDTOS = new ArrayList<>();

            for (ViewPostDTO viewPostDTO : viewPostDTOS) {
                Post post = pRepository.findById(viewPostDTO.getPostNo()).orElse(null);
                User writer = uRepository.findById(viewPostDTO.getUser().getUserNo()).orElse(null);
                postListDTOS.add(new PostListDTO(
                        isLike(user, post, isGuest),
                        viewPostDTO.getPostNo(),
                        viewPostDTO.getTitle(),
                        writer.getUserId(),
                        viewPostDTO.getWriteDateTime(),
                        cRepository.countAllByPost(post),
                        vRepository.countAllByPost(post)));
            }
            return postListDTOS;
    }

    public List<PostListDTO> getPageableRecoverPostList(Pageable pageable, HttpServletRequest req) {

        List<ViewPostDTO> viewPostDTOS = pRepository.getPageableRecoverList(pageable).getContent();
        List<PostListDTO> postListDTOS = new ArrayList<>();

        for (ViewPostDTO viewPostDTO : viewPostDTOS) {
            Post post = pRepository.findById(viewPostDTO.getPostNo()).orElse(null);
            User writer = uRepository.findById(viewPostDTO.getUser().getUserNo()).orElse(null);
            postListDTOS.add(new PostListDTO(
                    false,
                    viewPostDTO.getPostNo(),
                    viewPostDTO.getTitle(),
                    writer.getUserId(),
                    viewPostDTO.getWriteDateTime(),
                    cRepository.countAllByPost(post),
                    vRepository.countAllByPost(post)));
        }
        return postListDTOS;
    }

    public List<PostListDTO> getPageableSearchPostList(Pageable pageable, String title, HttpServletRequest req) {

        User user = new User();
        boolean isGuest = true;

        if(Objects.nonNull(req.getSession(false))) {
            user = uRepository.findByUserId((String) req.getSession(false).getAttribute("id")).orElse(null);
            isGuest = false;
        }

        List<ViewPostDTO> viewPostDTOS = pRepository.getPageableSearchList(pageable, title).getContent();
        List<PostListDTO> postListDTOS = new ArrayList<>();

        for (ViewPostDTO viewPostDTO : viewPostDTOS) {
            Post post = pRepository.findById(viewPostDTO.getPostNo()).orElse(null);
            User writer = uRepository.findById(viewPostDTO.getUser().getUserNo()).orElse(null);
            postListDTOS.add(new PostListDTO(
                isLike(user, post, isGuest),
                viewPostDTO.getPostNo(),
                viewPostDTO.getTitle(),
                writer.getUserId(),
                viewPostDTO.getWriteDateTime(),
                cRepository.countAllByPost(post),
                vRepository.countAllByPost(post)));
        }
        return postListDTOS;
    }

    public ContentDTO getContentByPostNo(Integer postNo) {

        PostContentDTO postContentDTO = pRepository.getContent(postNo);
        List<Comment> commentList = cRepository.findCommentListByPostNo(postNo);

        return new ContentDTO(postNo,
                postContentDTO.getTitle(),
                postContentDTO.getContent(),
                postContentDTO.getUser().getUserId(),
                postContentDTO.getWriteDateTime(),
                postContentDTO.getModifyDateTime(),
                commentList
                );
    }

    public OnlyTitleContentDTO getOnlyTitleContent(Integer postNo) {

        return pRepository.getOnlyModify(postNo);
    }

    @Transactional
    public void postRegister(String title, String content, HttpServletRequest req) {

        User user = uRepository.findByUserId((String) req.getSession(false).getAttribute("id")).orElse(null);
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

        Post post = pRepository.findById(postNo).orElse(null);
        post.setCheckHide(true);

        pRepository.save(post);
    }

    @Transactional
    public void postRecover(Integer postNo) {

        Post post = pRepository.findById(postNo).orElse(null);
        post.setCheckHide(false);

        pRepository.save(post);
    }

    @Transactional
    public void insertView(Integer postNo, HttpServletRequest req) {
        User user = uRepository.findByUserId((String)req.getSession(false).getAttribute("id")).orElse(null);
        Post post = pRepository.findById(postNo).orElse(null);

        View.ViewPK viewPK = new View.ViewPK(postNo, user.getUserNo());

        if(vRepository.existsById(viewPK)) {
            return;
        }

        View view = new View(viewPK, post, user);

        vRepository.save(view);
    }

    private boolean isLike(User user, Post post, boolean isGuest) {

        if (isGuest) {
            return false;
        }
        return lRepository.existsByPostAndUser(post, user);
    }
}
