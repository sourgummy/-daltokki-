package com.moon.daltokki.service;

import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.repository.UserRepository;
import com.moon.daltokki.sequrity.PrincipalDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.UUID;

public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); //google
        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId; // 사용자가 입력한 적은 없지만 만들어준다

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다

        String email = oAuth2User.getAttribute("email");
//        UserRole role = UserRole.ROLE_USER;

        UserModel byUseremail = userRepository.findByEmail(email);

        // DB에 없는 사용자라면 회원가입처리
//        if(byUseremail == null){
//            byUseremail = User.oauth2Register()
//                    .username(username).password(password).email(email).role(role)
//                    .provider(provider).providerId(providerId)
//                    .build();
//            userRepository.save(byUseremail);
//        }

        return new PrincipalDetails(byUseremail, oAuth2User.getAttributes());
    }

}
