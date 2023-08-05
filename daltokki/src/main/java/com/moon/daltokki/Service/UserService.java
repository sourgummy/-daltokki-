package com.moon.daltokki.Service;

import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.*;

@Slf4j
@Component
public class UserService {

  // 1. repo > crud
  @Autowired
  private UserRepository userRepository;

  // 2. template > sql
  @Autowired
  MongoTemplate mongoTemplate;

/*
  public String getPass(String id) { // id로 비밀번호 확인 (로그인)
    ObjectMapper objectMapper = new ObjectMapper();
    Optional<UserModel> userOptional = userRepository.findById(id);

    try {
      userOptional.ifPresent(user -> {
        log.info("[Service] user id : {} not exist!!", id);
        String pw = user.getPw();
        return pw;
      });
      if(!userOptional.isPresent()) {
        return objectMapper.writeValueAsString(userRepository.findById(id));
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
      return "ERROR";
    }

  }
*/

  public void userJoin(UserModel user) {
    String[] rabbit_array = {"hj1", "je1", "jk1", "mj1", "yr1"};
    List<String> rabbit_list = Arrays.asList(rabbit_array);
    Collections.shuffle(rabbit_list);
    rabbit_list.toArray(rabbit_array);
    String rabbit = rabbit_array[0];
    user.setRabbit_type(rabbit);
    user.setSp_record(0);
    user.setLogin_type("D");
    userRepository.save(user);
  }

}
