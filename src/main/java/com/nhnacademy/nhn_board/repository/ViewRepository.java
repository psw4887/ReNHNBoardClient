package com.nhnacademy.nhn_board.repository;

import com.nhnacademy.nhn_board.entity.Post;
import com.nhnacademy.nhn_board.entity.View;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViewRepository extends JpaRepository<View, View.ViewPK> {

    Integer countAllByPost(Post post);
}
