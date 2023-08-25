package com.moon.daltokki.Config;

import lombok.RequiredArgsConstructor;
import com.moon.daltokki.Service.OAuthService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

//  // 각각 유저가 있는지 확인하는 쿼리문, 유저의 권한을 확인하는 쿼리문
//  // configure(AuthenticationManagerBuilder auth) 메서드 대신
//  // PrincipalDetails 를 사용함
//
//  // 로그인 인가에 관한 설정
//  // 위에서는 로그인 가능한 아이디인지 여부를 확인한다면
//  // 아래에서는 일반 유저, 로그인 유저 등등이 어디서부터 어디까지 접근 가능한지
//  // 혹은 로그인과 로그아웃과 관련된 설정에 대한 부분을 여기서한다
//  @Bean
//  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//    http.csrf().disable()
//            .authorizeHttpRequests()
//            .antMatchers("/commList").hasRole("USER")
//            .antMatchers("/login/authtest").authenticated()
//            .and()
//            .formLogin().loginPage("/login").permitAll() // 커스텀 로그인 페이지
//            .defaultSuccessUrl("loginSuccess") // 로그인 성공시 기본 이동 페이지
//            // 로그인 form action url => /login 이라는 요청이 들어오면 시큐리티가 낚아채서 대신 로그인 진행
//            .loginProcessingUrl("/login")
////			.usernameParameter("mid") // 로그인폼에서 id 를 적는 곳에 특정한 name을 사용한다면 여기서 변경
////			.passwordParameter("mpwd") // 로그인폼에서 password 를 적는 곳에 특정한 name을 사용한다면 여기서 변경
//            .and()
//            .logout().logoutUrl("/logout").permitAll()
//            .logoutSuccessUrl("/") // 로그아웃 성공 시 이동 페이지
//            .and()
//            .oauth2Login().loginPage("/login"); // 외부 인증
//
//  }
//        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll()) // 모든 인증되지 않은 요청을 허락
//      .csrf((csrf) -> csrf
//        .ignoringRequestMatchers(new AntPathRequestMatcher("/**"))) // 스프링 시큐리티가 CSRF 처리시 예외로 처리
//      .headers((headers) -> headers
//        .addHeaderWriter(new XFrameOptionsHeaderWriter(
//            XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))) // 사이트 콘텐츠 포함
//      .formLogin((formLogin) -> formLogin
//        .loginPage("/login") // 로그인 페이지
//        .defaultSuccessUrl("/loginSuccess")) // main?id=userId로 이동
//      .logout((logout) -> logout
//        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//        .logoutSuccessUrl("/")
//        .invalidateHttpSession(true))
//      .oauth2Login() // 아 환장하겟서 진짜
//        .loginPage("/loginForm")
//        .defaultSuccessUrl("/loginSuccess")


  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

}

