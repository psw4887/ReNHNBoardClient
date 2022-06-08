package com.nhnacademy.nhn_board.dto.complete;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class PostListDTO {

    private Boolean isLike;

    private Integer postNo;

    private String title;

    private String writer;

    private LocalDateTime writeDateTime;

    private Integer commentCount;

    private Integer viewCount;
}
