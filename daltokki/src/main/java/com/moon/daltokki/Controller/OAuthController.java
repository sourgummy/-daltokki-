package com.moon.daltokki.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.moon.daltokki.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

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


