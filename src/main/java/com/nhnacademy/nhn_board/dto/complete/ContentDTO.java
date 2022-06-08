package com.nhnacademy.nhn_board.dto.complete;

import com.nhnacademy.nhn_board.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ContentDTO {

    private int postNo;

    private String title;

    private String content;

    private String writer;

    private LocalDateTime writeDatetime;

    private LocalDateTime modifyDatetime;

    List<Comment> commentList;
}
