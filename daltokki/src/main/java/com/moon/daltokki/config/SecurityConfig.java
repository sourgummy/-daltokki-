package com.moon.daltokki.config;

import com.moon.daltokki.service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // 비밀번호 암호화
import org.springframework.security.crypto.password.PasswordEncoder; // 비밀번호 암호화

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  OAuthService oAuthService;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
        )
        .csrf(csrf ->
            csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/**"))
        )
        .headers(headers ->
            headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
        )
        .formLogin(formLogin ->
            formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/loginSuccess")
        )
        .oauth2Login(oauth2Login -> // 구글 로그인
            oauth2Login
                .loginPage("/oauth2/authorization/google")
                .defaultSuccessUrl("/googleLogin") // login?error로 매핑됨
                .failureUrl("/loginForm") // 로그인 실패 시 /loginForm으로 이동
        )
//        .oauth2Login(oauth2Login -> // 네이버 로그인
//            oauth2Login
//                .loginPage("/oauth2/authorization/naver") // 네이버 로그인 URL
//                .defaultSuccessUrl("/login/oauth2/code/naver") // 로그인 성공 시 리다이렉트될 URL
//                .failureUrl("/loginForm") // 로그인 실패 시 리다이렉트될 URL
////                .userInfoEndpoint() // 사용자 정보 가져오기 설정
////                .userService(new NaverOAuth2UserService()) // 사용자 정보를 가져오는 서비스
//        )

        .logout(logout ->
            logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
        );

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}

