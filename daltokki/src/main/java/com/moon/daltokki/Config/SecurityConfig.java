package com.moon.daltokki.Config;

import com.moon.daltokki.Service.OAuthService;
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

  // 얘를 어디서부터 손봐야하지.. - 기존 filterChain
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
//      .oauth2Login() // 아 환장하겟서 진짜
//        .loginPage("/loginForm")
//        .defaultSuccessUrl("/loginSuccess")
//        .userInfoEndpoint() // 일정 기간지나면 자동 로그아웃
//        .userService(oAuthService); // 구글 로그인이 완료된(구글회원) 뒤의 후처리가 필요함 . Tip.코드x, (엑세스 토큰+사용자 프로필 정보를 받아옴)
    ;
    return http.build();
  }

//  @Bean
//  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http.csrf().disable()
//            .authorizeRequests()
//            .antMatchers("/user/**").authenticated()
//            .antMatchers("/manager/**").hasAuthority("MANAGER")
//            .antMatchers("/admin/**").hasAuthority("ADMIN")
//            .anyRequest().permitAll()
//
//            .and()
//            .formLogin()
//            .loginPage("/loginForm") // 미인증자일경우 해당 uri를 호출
//            .loginProcessingUrl("/login") //login 주소가 호출되면 시큐리티가 낚아 채서(post로 오는것) 대신 로그인 진행 -> 컨트롤러를 안만들어도 된다.
//            .defaultSuccessUrl("/loginSuccess") // main?id=userId로 이동
//
//            .and()
//            .oauth2Login()
//            .loginPage("/loginForm")
//            .defaultSuccessUrl("/")
//            .userInfoEndpoint()
//            .userService(principalOauthUserService);//구글 로그인이 완료된(구글회원) 뒤의 후처리가 필요함 . Tip.코드x, (엑세스 토큰+사용자 프로필 정보를 받아옴)
//    return http.build();
//  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}
