package com.nhnacademy.nhn_board.controller;

import com.nhnacademy.nhn_board.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService service;

    @PostMapping("/check")
    public String insertLike(@RequestParam("postNo") Integer postNo,
                             @RequestParam("userNo") Integer userNo,
                             @RequestParam("page") Integer page) {

        if(service.isLike(postNo, userNo)) {
            service.deleteLike(postNo, userNo);
        } else {
            service.insertLike(postNo, userNo);
        }

        return "redirect:/board/view?page=" + page;
    }
}
