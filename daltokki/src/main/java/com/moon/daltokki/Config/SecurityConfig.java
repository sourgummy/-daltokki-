package com.moon.daltokki.Config;

import com.moon.daltokki.Service.OAuthService;
import com.moon.daltokki.Service.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // 비밀번호 암호화
import org.springframework.security.crypto.password.PasswordEncoder; // 비밀번호 암호화

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Autowired
  OAuthService oAuthService;

//  @Autowired
//  PrincipalOauth2UserService principalOauth2UserService;

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
//        .oauth2Login(oauth2Login ->
//            oauth2Login
//                .loginPage("/login")
////                .defaultSuccessUrl("/login/oauth2/code/google") // login?error로 매핑됨
//                .defaultSuccessUrl("loginSuccess") // login?error로 매핑됨
//                .failureUrl("/loginForm")		// 로그인 실패 시 /loginForm으로 이동
//                .userInfoEndpoint()			// 로그인 성공 후 사용자정보를 가져온다
//                .userService(principalOauth2UserService)	//사용자정보를 처리할 때 사용한다
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
