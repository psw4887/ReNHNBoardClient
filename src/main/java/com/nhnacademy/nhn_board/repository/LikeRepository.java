package com.nhnacademy.nhn_board.repository;

import com.nhnacademy.nhn_board.entity.Like;
import com.nhnacademy.nhn_board.entity.Post;
import com.nhnacademy.nhn_board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {

    Boolean existsByPostAndUser(Post post, User user);

    @Query("select l from Like as l where l.post.postNo = ?1 and l.user.userNo = ?2")
    Like findLike(Integer postNo, Integer userNo);
}
