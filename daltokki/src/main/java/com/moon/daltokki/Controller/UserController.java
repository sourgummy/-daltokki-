package com.moon.daltokki.Controller;

import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/login") // 로그인 페이지
    public String login() {
        System.out.println("userLogin");
        log.info("UserController");
        return "user/login";
    }
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
