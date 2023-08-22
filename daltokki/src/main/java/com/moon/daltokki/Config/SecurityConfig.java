package com.moon.daltokki.Config;

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

  // 얘를 어디서부터 손봐야하지..
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 모든 인증되지 않은 요청을 허락
      .csrf((csrf) -> csrf
        .ignoringRequestMatchers(new AntPathRequestMatcher("/**"))) // 스프링 시큐리티가 CSRF 처리시 예외로 처리
      .headers((headers) -> headers
        .addHeaderWriter(new XFrameOptionsHeaderWriter(
            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))) // 사이트 콘텐츠 포함
      .formLogin((formLogin) -> formLogin
        .loginPage("/login") // 로그인 페이지
        .defaultSuccessUrl("/loginSuccess")) // main?id=userId로 이동
      .logout((logout) -> logout
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true))
    ;
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
