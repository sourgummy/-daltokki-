package com.moon.daltokki.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.service.OAuthService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//@Controller
//@ResponseBody
//public class OAuthController {
//   @Autowired
//    private UserService userService; // UserService 주입
//      private final Logger log = LoggerFactory.getLogger(OAuthController.class);
//  @GetMapping("/oauth/login")
//  @ResponseBody
//  public String naverOAuthRedirect(@RequestParam String code, @RequestParam String state, Model model) {
//    // RestTemplate 인스턴스 생성
//    RestTemplate rt = new RestTemplate();
//
//    HttpHeaders accessTokenHeaders = new HttpHeaders();
//    accessTokenHeaders.add("Content-Type", "application/x-www-form-urlencoded");
//
//    MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
//    accessTokenParams.add("grant_type", "authorization_code");
//    accessTokenParams.add("client_id", "vmZ60MBkDiJfUnPtrRxQ");
//    accessTokenParams.add("client_secret", "mojUe4sbSf");
//    accessTokenParams.add("code", code);  // 응답으로 받은 코드
//    accessTokenParams.add("state", state); // 응답으로 받은 상태
//    log.info(code);
//      System.out.println("여기도착함?");
//    HttpEntity<MultiValueMap<String, String>> accessTokenRequest = new HttpEntity<>(accessTokenParams, accessTokenHeaders);
//
//    ResponseEntity<String> accessTokenResponse = rt.exchange(
//        "https://nid.naver.com/oauth2.0/token",
//        HttpMethod.POST,
//        accessTokenRequest,
//        String.class
//    );
//    return "accessToken: " + accessTokenResponse.getBody();
//
//    }
//}

@Slf4j
@Controller
public class OAuthController {
    private final Logger log = LoggerFactory.getLogger(OAuthController.class);
    @GetMapping("/oauth/login")
    public String NCallback(Model model, @RequestParam("code") String code, @RequestParam("state") String state) {
        String clientId = "vmZ60MBkDiJfUnPtrRxQ";
        String clientSecret = "mojUe4sbSf";
        String redirectURI = URLEncoder.encode("http://localhost:8080/oauth/login");
        String apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
        apiURL += "client_id=" + clientId;
        apiURL += "&client_secret=" + clientSecret;
        apiURL += "&redirect_uri=" + redirectURI;
        apiURL += "&code=" + code;
        apiURL += "&state=" + state;
        String responseBody = callNaverAPI(apiURL);

        log.info("**** code ****" + code);
        System.out.println("여기도착함?");

        model.addAttribute("responseBody", responseBody);
        return "user/NCallback";
    }

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

    private String callNaverAPI(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader br;

            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuffer res = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                res.append(inputLine);
            }
            br.close();
            return res.toString();
        } catch (Exception e) {
            System.out.println(e);
            return "";
        }
    }

    //code 없음
//    @RequestMapping(value="/oauth/login", method= RequestMethod.GET)
//    public String loginPOSTNaver(HttpSession session) {
//        log.info("OAuth-callback controller");
//        return "user/callback";
//    }

}


