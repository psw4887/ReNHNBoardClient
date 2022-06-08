package com.nhnacademy.nhn_board.dto;

import com.nhnacademy.nhn_board.entity.User;

public interface OnlyTitleContentDTO {

    User getUser();

    String getTitle();

    String getContent();
}
