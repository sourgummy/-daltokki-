package com.moon.daltokki.Controller;

import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/")
    public String index() { return "main"; }

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

}
