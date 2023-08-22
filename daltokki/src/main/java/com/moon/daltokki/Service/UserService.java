package com.moon.daltokki.Service;

import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Slf4j
@Component
public class UserService {

  // 1. repo > crud
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  // 2. template > sql
  @Autowired
  MongoTemplate mongoTemplate;

/*  public String getPass(String username) { // id로 비밀번호 확인 (로그인)
    System.out.println(username);
    String password = "";
    password += userRepository.selectPass(username);
    System.out.println(password);
    return password;
  }*/

  public void joinPro(UserModel user) { // 회원 가입
    user.setPassword(passwordEncoder.encode(user.getPassword())); // 비밀번호 암호화
    // 토끼 유형 결정
    String[] rabbit_array = {"hj1", "je1", "jk1", "mj1", "yr1"};
    List<String> rabbit_list = Arrays.asList(rabbit_array);
    Collections.shuffle(rabbit_list);
    rabbit_list.toArray(rabbit_array);
    String rabbit = rabbit_array[0];
    user.setRabbitType(rabbit); // 토끼 유형 세팅
    user.setSpRecord(0); // 송편 개수 0
    user.setLoginType("D"); // 기본 가입 유형 "D"
    System.out.println(user);
    userRepository.save(user);
  }

  // ----------------- 지은 -------------------
  // id 존재 여부 확인
  public boolean checkIdExists(String id) {
    log.info("[Service][checkIdExists] Checking if ID exists: {}", id);

    return userRepository.existsByUsername(id);
  }

  // 사용자의 정보 조회
  public Optional<UserModel> selectUserInfo(String id) {
    log.info("[Service][selectUserInfo] selected username : {}", id);

    return userRepository.findByusername(id);
  }

  // 토끼타입 갱신
//  public String updateRabbitType(String rabbitType) {
  public void updateRabbitType(String rabbitType) {
    log.info("[Service][selectUserInfo] updateRabbitType : {}", rabbitType);

//    return userRepository.saverabbitType(rabbitType); // 오류나서 수정해야함
  }
  // ----------------- 지은 -------------------

}
