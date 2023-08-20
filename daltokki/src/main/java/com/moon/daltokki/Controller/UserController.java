package com.moon.daltokki.Controller;

import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.authenticator.SavedRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/login") // 로그인 페이지
    public String login() {
        return "user/login";
    }
    /*@PostMapping(value = "/loginPro") // 로그인 로직
    public String loginPro(
        @ModelAttribute UserModel user
        , Model model
        , HttpSession session
        ) {
        System.out.println(user);
        if(user.getPassword() == userService.getPass(user.getUsername())) {
            session.setAttribute("sId", user.getUsername());
            return "redirect:/";
        } else {
            model.addAttribute("msg", "사용자 정보가 일치하지 않습니다.");
            return "fail_back";
        }

    }*/
    @GetMapping(value = "/join") // 가입 페이지
    public String join() {
        return "user/join";
    }
    @PostMapping(value = "/joinPro") // 가입 로직
    public String joinPro(
        @ModelAttribute UserModel user
        , Model model
    ) {
        userService.joinPro(user);
        return "redirect:/";
    }

    // -----------------------------------------

    // 첫화면
    @GetMapping("/")
    public String redirectToindex() {
        return "main";
    }

    // 로그인시 사용자 식별 정보가 포함된 url 생성 - 지은
    @GetMapping("/loginSuccess")
    public String loginSuccessRedirect() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = auth.getName();

        String mainUrl = "/main?id=" + userId;
        return "redirect:" + mainUrl;
    }
    // -----------------------------------------
}
