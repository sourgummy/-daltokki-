package com.moon.daltokki.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;

    public OAuthService(Environment env) {
        this.env = env;
    }

    public UserModel GoogleSocialLogin(OAuth2AuthenticationToken oauth2Authentication) {
        System.out.println("GoogleSocialLogin");

        String tokenCode = oauth2Authentication.getName();
        String name = (String)oauth2Authentication.getPrincipal().getAttribute("name");
        String email = oauth2Authentication.getPrincipal().getAttribute("email");
        String id = email.split("@")[0];
        log.info("[OAuthService][googleLoginSuccess] tokenCode : {}", tokenCode);
        log.info("[OAuthService][googleLoginSuccess] name : {}", name);
        log.info("[OAuthService][googleLoginSuccess] email : {}", email);
        log.info("[OAuthService][googleLoginSuccess] id : {}", id);

        UserModel user = new UserModel();
        String loginType = "G"; // 여기서 registrationId로 (google/kakao/naver) 받아와야함
        log.info("[OAuthService][GoogleSocialLogin] loginType : {}",loginType);

        Criteria criteria = new Criteria();
        criteria.andOperator(
                Criteria.where("email").is(email),
                Criteria.where("loginType").is(loginType)
        );

        Query query = new Query(criteria);
        log.info("[OAuthService][GoogleSocialLogin] query : {}", query);

        List<UserModel> resultList = mongoTemplate.find(query, UserModel.class);
        boolean result = !resultList.isEmpty();
        log.info("[OAuthService][GoogleSocialLogin] result : {}", result);

        if(!result) {
//        if(!userRepository.existsByEmail(email, loginType)) {
//        if(!userRepository.existsByEmail(email)) { // 동일한 이메일이 없으면 DB에 신규 등록
            // username, password, nickname, sp_record, login_type, rabbit_type
            System.out.println("어디로 오는거임1");
            String[] rabbit_array = {"hj1", "je1", "jk1", "mj1", "yr1"};
            List<String> rabbit_list = Arrays.asList(rabbit_array);
            Collections.shuffle(rabbit_list);
            rabbit_list.toArray(rabbit_array);
            String rabbit = rabbit_array[0];
            id += loginType;

            user.setUsername(id); // 회원 아이디 
            user.setToken(tokenCode); // 토큰
            user.setNickname(name); // 닉네임
            user.setRabbitType(rabbit); // 토끼 유형 세팅
            user.setEmail(email); // 로그인 이메일
            user.setSpRecord(0); // 송편 개수
            user.setLoginType(loginType); // 가입 유형
            System.out.println(user);
            userRepository.save(user);
            log.info("[OAuthService][GoogleSocialLogin] userInfo : {}", user);
        }
        else { // 이미 가입된 상태면
            System.out.println("어디로 오는거임2");
            // 로그인 처리는 시큐리티가 자동
            // find는 List로 반환, findOne은 첫번째 요소 1행만 반환
            user = mongoTemplate.findOne(query, UserModel.class);
            log.info("[OAuthService][GoogleSocialLogin] user : {}", user);
        }
        return user;
    }

    // ------------------------ 위가 새로 짠 코드 --------------------

    public UserModel GoogleSocialLogin2(String code, String registrationId) {
        System.out.println("GoogleSocialLogin2");
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
            // 로그인 처리는 좀 더 생각해보자ㅇ.
            user = userRepository.findByEmail(email); // 아 여기서 걸린다
        }
        return user;
    }

    private String getAccessToken(String authorizationCode, String registrationId) {
        System.out.println("getAccessToken");
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

        System.out.println("어디0?");
        // application-oauth.properties 요소 수정 => env.getProperty()로 가져올때 이름과 동일해야함
        // 108라인 에서 에러터짐
        ResponseEntity<JsonNode> responseNode = restTemplate.exchange(tokenUri, HttpMethod.POST, entity, JsonNode.class);
        System.out.println("어디1?");
        JsonNode accessTokenNode = responseNode.getBody();
        System.out.println("어디2?");
        return accessTokenNode.get("access_token").asText();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        System.out.println("getUserResource");
        String resourceUri = env.getProperty("oauth2."+registrationId+".resource-uri");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity entity = new HttpEntity(headers);
        return restTemplate.exchange(resourceUri, HttpMethod.GET, entity, JsonNode.class).getBody();
    }
}
