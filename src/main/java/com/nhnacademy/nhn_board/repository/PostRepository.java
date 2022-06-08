package com.nhnacademy.nhn_board.repository;

import com.nhnacademy.nhn_board.dto.OnlyTitleContentDTO;
import com.nhnacademy.nhn_board.dto.PostContentDTO;
import com.nhnacademy.nhn_board.dto.ViewPostDTO;
import com.nhnacademy.nhn_board.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    @Query("select p from Post as p where p.postNo = ?1")
    OnlyTitleContentDTO getOnlyModify(int postNo);
}
