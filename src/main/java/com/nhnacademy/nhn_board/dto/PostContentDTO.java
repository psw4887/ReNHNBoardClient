package com.nhnacademy.nhn_board.dto;

import com.nhnacademy.nhn_board.entity.User;

import java.time.LocalDateTime;

public interface PostContentDTO {

    User getUser();

    String getTitle();

    String getContent();

    LocalDateTime getWriteDateTime();

    LocalDateTime getModifyDateTime();
}
