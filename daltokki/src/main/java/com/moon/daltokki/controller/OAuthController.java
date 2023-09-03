package com.moon.daltokki.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moon.daltokki.sequrity.NaverOauthParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.service.OAuthService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Slf4j
@Controller
public class OAuthController {
   // -------------------네아로----------------------------
   @GetMapping("login/oauth2/code/naver")
   @ResponseBody
   public String naverOAuthRedirect(@RequestParam String code, @RequestParam String state) {
//      return "code : " + code;

    // RestTemplate 인스턴스 생성
        RestTemplate rt = new RestTemplate();

        HttpHeaders accessTokenHeaders = new HttpHeaders();
        accessTokenHeaders.add("Content-type", "application/x-www-form-urlencoded");

        MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
        accessTokenParams.add("grant_type", "authorization_code");
        accessTokenParams.add("client_id", "vmZ60MBkDiJfUnPtrRxQ");
        accessTokenParams.add("client_secret", "mojUe4sbSf");
        accessTokenParams.add("code" , code);	// 응답으로 받은 코드
        accessTokenParams.add("state" , state); // 응답으로 받은 상태

        HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, accessTokenHeaders);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                accessTokenRequest,
                String.class
        );

//        return "accessToken: " + accessTokenResponse.getBody();
//    }
     // 이전에 받았던 Access Token 응답
    ObjectMapper objectMapper = new ObjectMapper();

    // json -> 객체로 매핑하기 위해 NaverOauthParams 클래스 생성
    NaverOauthParams naverOauthParams = null;
    try {
        naverOauthParams = objectMapper.readValue(accessTokenResponse.getBody(), NaverOauthParams.class);
    } catch (JsonProcessingException e) {
        e.printStackTrace();
    }

    // header 생성해서 access token 넣기
    HttpHeaders profileRequestHeader = new HttpHeaders();
    profileRequestHeader.add("Authorization", "Bearer " + naverOauthParams.getAccess_token());

    HttpEntity<HttpHeaders> profileHttpEntity = new HttpEntity<>(profileRequestHeader);

    // profile api로 생성해둔 헤더를 담아서 요청 보내기
    ResponseEntity<String> profileResponse = rt.exchange(
            "https://openapi.naver.com/v1/nid/me",
            HttpMethod.POST,
            profileHttpEntity,
            String.class
    );

	return "profile response : " + profileResponse.getBody();
//     return "user/callback";
}

//-------------- 네아로 -----------------------------------


  // ----------------- 지은 0901 수정 -------------------
    @Autowired
    private OAuthService oAuthService;

    // 아니 로그인은 되는데 시큐리티에 세션이 안넘어감.. 하..
    // -> 시큐리티에는 토큰이 저장되는데 id랑 비교를 하고 있었으니까 ^ㅅ^.. 히밣.. 디버깅 열심히 해요..
    @GetMapping("/googleLogin")
    public String googleLoginSuccess(Authentication authentication, Model model) {
        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Authentication = (OAuth2AuthenticationToken) authentication;

            UserModel user = oAuthService.GoogleSocialLogin(oauth2Authentication);
            log.info("[OAuthController][googleLogin] user : {}", user);

            String GoogleLoginId = user.getUsername();
            log.info("[OAuthController][googleLogin] GoogleLoginId : {}", GoogleLoginId);

//            String tokenCode = user.getToken();
//            log.info("[OAuthController][googleLogin] tokenCode : {}", tokenCode);
//
//            model.addAttribute("tokenCode", tokenCode);
//            log.info("[OAuthController][googleLogin] model_tokenCode : {}", model.getAttribute("tokenCode")); // null

            // model이 안넘어가는건가..?
            return "redirect:/main?id=" + GoogleLoginId;
        }
        // 로그인 실패 시 로그인 폼 페이지로 이동
        return "redirect:/loginForm";
    }
    
  // ----------------- 지은 0901 구글 api 수정 -------------------



}


