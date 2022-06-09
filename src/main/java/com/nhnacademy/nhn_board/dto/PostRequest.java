package com.nhnacademy.nhn_board.dto;

import com.nhnacademy.nhn_board.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostRequest {

    private User user;

    private String title;

    private String content;
}
