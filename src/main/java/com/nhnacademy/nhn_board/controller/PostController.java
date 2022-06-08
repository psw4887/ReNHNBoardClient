package com.nhnacademy.nhn_board.controller;

import com.nhnacademy.nhn_board.dto.OnlyTitleContentDTO;
import com.nhnacademy.nhn_board.dto.complete.ContentDTO;
import com.nhnacademy.nhn_board.dto.complete.PostListDTO;
import com.nhnacademy.nhn_board.entity.User;
import com.nhnacademy.nhn_board.service.PostService;
import com.nhnacademy.nhn_board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class PostController {

    private final PostService pService;
    private final UserService uService;

    @GetMapping("/view")
    String goBoard(@RequestParam("page") Integer page,
                   Model model,
                   HttpServletRequest req) {

        User user = new User();

        if(Objects.nonNull(req.getSession(false))) {
            user = uService.findUserById((String) req.getSession(false).getAttribute("id"));
        }

        PageRequest pageRequest = PageRequest.of(page, 20);
        List<PostListDTO> list = pService.getPageablePostList(pageRequest, req);
        model.addAttribute("isEnd", 0);

        if(list.size() < 20) {
            model.addAttribute("isEnd", 1);
        }
        model.addAttribute("page", page);
        model.addAttribute("lists", list);
        model.addAttribute("user", user);

        return "viewBoard";
    }

    @GetMapping("/content")
    String viewContent(@RequestParam("postNo") int postNo,
                       HttpServletRequest req,
                       Model model) {

        if(Objects.nonNull(req.getSession(false))) {
            pService.insertView(postNo, req);
        }

        ContentDTO contentDTO = pService.getContentByPostNo(postNo);

        model.addAttribute("dto", contentDTO);

        return "boardContent";
    }

    @GetMapping("/register")
    String readyRegisterPost(HttpServletRequest req) {

        if (Objects.isNull(req.getSession(false))) {
            return "redirect:/board/view?page=0";
        }
        return "boardRegisterForm";
    }

    @PostMapping("/register")
    String registerPost(@RequestParam("writeTitle") String title,
                        @RequestParam("writeContent") String content,
                        HttpServletRequest req) {

        pService.postRegister(title, content, req);

        return "redirect:/board/view?page=0";
    }

    @GetMapping("/modify/{postNo}")
    String readyModifyPost(HttpServletRequest req,
                           @PathVariable("postNo") int postNo,
                           Model model) {

        OnlyTitleContentDTO dto = pService.getOnlyTitleContent(postNo);

        if (Objects.isNull(req.getSession(false)) ||
                (!((String)(req.getSession(false).getAttribute("id"))).equals(dto.getUser().getUserId()))) {
            return "redirect:/board/content?postNo=" + postNo;
        }

        model.addAttribute("postNo", postNo);
        model.addAttribute("title", dto.getTitle());
        model.addAttribute("content", dto.getContent());

        return "boardModifyForm";
    }

    @PostMapping("/modify")
    String modifyPost(@RequestParam("title") String title,
                      @RequestParam("content") String content,
                      @RequestParam("postNo") Integer postNo) {

        pService.postModify(title, content, postNo);

        return "redirect:/board/content?postNo=" + postNo;
    }

    @PostMapping("/delete/{postNo}")
    String deletePost(HttpServletRequest req,
                      @PathVariable("postNo") Integer postNo) {

        String userId = (String)req.getSession(false).getAttribute("id");

        OnlyTitleContentDTO dto = pService.getOnlyTitleContent(postNo);

        if (Objects.isNull(req.getSession(false)) ||(
                (!userId.equals(dto.getUser().getUserId())) &&
                (!userId.equals("admin")))) {
            return "redirect:/board/content?postNo=" + postNo;
        }

        pService.postDelete(postNo);

        return "redirect:/board/view?page=0";
    }

    @PostMapping("/search")
    String searchPost(@RequestParam("title") String title,
                      @RequestParam("page") Integer page,
                      HttpServletRequest req,
                      Model model) {

        if (Objects.isNull(req.getSession(false))) {
            return "redirect:/board/view?page=0";
        }
        User user = uService.findUserById((String) req.getSession(false).getAttribute("id"));

        PageRequest pageRequest = PageRequest.of(page, 20);
        List<PostListDTO> lists = pService.getPageableSearchPostList(pageRequest, title, req);
        model.addAttribute("isEnd", 0);

        if(lists.size() < 20) {
            model.addAttribute("isEnd", 1);
        }
        model.addAttribute("page", page);
        model.addAttribute("searchList", lists);
        model.addAttribute("user", user);

        return "boardSearch";
    }

    @GetMapping("/recover")
    String readyRecoverPost(@RequestParam("page") Integer page,
                       HttpServletRequest req,
                       Model model) {

        if (Objects.isNull(req.getSession(false)) ||
                (!((String)(req.getSession(false).getAttribute("id"))).equals("admin"))) {
            return "redirect:/board/view?page=0";
        }

        PageRequest pageRequest = PageRequest.of(page, 20);
        List<PostListDTO> lists = pService.getPageableRecoverPostList(pageRequest, req);
        model.addAttribute("isEnd", 0);

        if(lists.size() < 20) {
            model.addAttribute("isEnd", 1);
        }
        model.addAttribute("page", page);
        model.addAttribute("recoverList", lists);

        return "boardRecover";
    }

    @PostMapping("/recover/{postNo}")
    String recoverPost(@PathVariable("postNo") Integer postNo) {

        pService.postRecover(postNo);

        return "redirect:/board/recover?page=0";
    }
}
