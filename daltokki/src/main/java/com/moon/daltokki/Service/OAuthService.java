package com.moon.daltokki.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
@PropertySource("classpath:application-oauth.properties")
public class OAuthService {
    private final Environment env;
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private UserRepository userRepository;

    public OAuthService(Environment env) {
        this.env = env;
    }
    
    // 테스트용 -> 잘넘어옴
//    public void GoogleSocialLogin(String code, String registrationId) {
//        System.out.println("code = " + code);
//        System.out.println("registrationId = " + registrationId);
//    }

    public UserModel GoogleSocialLogin(String code, String registrationId) {
        String accessToken = getAccessToken(code, registrationId);
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);
        System.out.println("userResourceNode = " + userResourceNode);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String nickname = userResourceNode.get("name").asText();
        System.out.println("id = " + id);
        System.out.println("email = " + email);
        System.out.println("nickname = " + nickname);

        UserModel user = new UserModel();

        if(!userRepository.existsByEmail(email)) { // 동일한 이메일이 없으면 DB에 신규 등록
            // username, password, nickname, sp_record, login_type, rabbit_type
            String[] rabbit_array = {"hj1", "je1", "jk1", "mj1", "yr1"};
            List<String> rabbit_list = Arrays.asList(rabbit_array);
            Collections.shuffle(rabbit_list);
            rabbit_list.toArray(rabbit_array);
            String rabbit = rabbit_array[0];
            String[] emailSplit = email.split("@");
            String email2 = emailSplit[0];
//            String[] email2 = email.split("@");

            log.info("[Service][GoogleSocialLogin] email2 : {}", email2);
            user.setUsername(email2); // 회원 아이디 (여기에 이메일을 저장해야하나..?)
//            user.setUsername(id);
            user.setNickname(nickname); // 닉네임
            user.setRabbitType(rabbit); // 토끼 유형 세팅
            user.setEmail(email); // 구글 로그인 이메일
            user.setSpRecord(0); // 송편 개수 0
            user.setLoginType("G"); // 구글 가입 유형 "G"
            System.out.println(user);
            userRepository.save(user);
            log.info("[OAuthService][GoogleSocialLogin] userInfo : {}", user);
        }
        else { // 이미 가입된 상태면
            // 로그인 처리는 좀 더 생각해보자ㅇ..
            user = userRepository.findByEmail(email);
        }
        return user;
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        String clientId = env.getProperty("oauth2." + registrationId + ".client-id");
        String clientSecret = env.getProperty("oauth2." + registrationId + ".client-secret");
        String redirectUri = env.getProperty("oauth2." + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty("oauth2." + registrationId + ".token-uri");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity entity = new HttpEntity(params, headers);

        // application-oauth.properties 요소 수정 => env.getProperty()로 가져올때 이름과 동일해야함
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        JsonNode accessTokenNode = responseNode.getBody();
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}
