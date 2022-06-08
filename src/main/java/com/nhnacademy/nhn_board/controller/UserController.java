package com.nhnacademy.nhn_board.controller;

import com.nhnacademy.nhn_board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/login")
    public String readyLogin(HttpServletRequest req) {

        if(Objects.isNull(req.getSession(false))) {
            return "loginForm";
        }

        return "redirect:/index";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam("id")String id,
                          @RequestParam("pw")String pw,
                          HttpServletRequest req) {

        if(service.successLogin(id, pw)) {
            req.getSession().setAttribute("id", id);

            return "redirect:/index";
        }

        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String doLogout(HttpServletRequest req) {

        if(Objects.nonNull(req.getSession(false))) {
            req.getSession(false).invalidate();
        }

        return "redirect:/index";
    }
}
