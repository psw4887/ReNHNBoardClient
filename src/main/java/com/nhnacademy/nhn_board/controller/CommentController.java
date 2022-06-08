package com.nhnacademy.nhn_board.controller;

import com.nhnacademy.nhn_board.entity.Comment;
import com.nhnacademy.nhn_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService service;

    @PostMapping("/register/{postNo}")
    public String commentRegister(@PathVariable("postNo")Integer postNo,
                                  @RequestParam("comment")String comment,
                                  HttpServletRequest req) {
        if (Objects.isNull(req.getSession(false))) {
            return "redirect:/board/content?postNo=" + postNo;
        }

        service.registerComment(postNo, comment, req);

        return "redirect:/board/content?postNo=" + postNo;
    }

    @GetMapping("/{postNo}/{commentNo}")
    public String readyCommentModify(@PathVariable("postNo") Integer postNo,
                                     @PathVariable("commentNo") Integer commentNo,
                                     @RequestParam("button") String btn,
                                     HttpServletRequest req,
                                     Model model) {

        if (Objects.isNull(req.getSession(false))) {
            return "redirect:/board/content?postNo=" + postNo;
        }

        Comment comment = service.getComment(commentNo);

        if (!((String) (req.getSession(false).getAttribute("id"))).equals(
            comment.getUser().getUserId())) {
            return "redirect:/board/content?postNo=" + postNo;
        }

        if ("Modify".equals(btn)) {
            model.addAttribute("postNo", postNo);
            model.addAttribute("comment", comment);

            return "commentModifyForm";

        } else if ("Delete".equals(btn)) {
            service.deleteComment(commentNo);
        }

        return "redirect:/board/content?postNo=" + postNo;
    }

    @PostMapping("modify/{postNo}/{commentNo}")
    public String commentModify(@PathVariable("postNo") Integer postNo,
                                @PathVariable("commentNo") Integer commentNo,
                                @RequestParam("content") String content) {

        service.modifyComment(commentNo, content);

        return "redirect:/board/content?postNo=" + postNo;
    }
}
