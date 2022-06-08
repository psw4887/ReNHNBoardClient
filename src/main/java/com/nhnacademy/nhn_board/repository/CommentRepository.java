package com.nhnacademy.nhn_board.repository;

import com.nhnacademy.nhn_board.entity.Comment;
import com.nhnacademy.nhn_board.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Integer countAllByPost(Post post);

    @Query("select c from Comment as c where c.post.postNo = ?1")
    List<Comment> findCommentListByPostNo(int postNo);
}
