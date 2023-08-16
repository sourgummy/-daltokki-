package com.moon.daltokki.Controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller

public class WebErrorController implements ErrorController {

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if(status != null){
            int statusCode = Integer.valueOf(status.toString());

            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                String message = "400 - 잘못된 요청이 있습니다.";
                model.addAttribute("message", message);
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                String message = "405 - 지원하지 않는 HTTP 메서드입니다.";
                model.addAttribute("message", message);
            } else {
                return "error/error";
            }
        }
        return "error/error";
    }

}
