package com.nhnacademy.nhn_board.dto;

import com.nhnacademy.nhn_board.entity.User;

import java.time.LocalDateTime;

public interface ViewPostDTO {

    Integer getPostNo();

    User getUser();

    String getTitle();

    LocalDateTime getWriteDateTime();
}
