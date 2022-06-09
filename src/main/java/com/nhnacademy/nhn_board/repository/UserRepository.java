package com.nhnacademy.nhn_board.repository;

import com.nhnacademy.nhn_board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByUserIdAndUserPw(String id, String pw);
}
