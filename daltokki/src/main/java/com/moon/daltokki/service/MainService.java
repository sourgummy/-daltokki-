package com.moon.daltokki.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.moon.daltokki.Model.SpModel;
import com.moon.daltokki.repository.SpRepository;
import com.moon.daltokki.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class MainService {

    @Autowired
    private SpRepository spRepository;

    @Autowired
    private UserRepository userRepository;

    // ~~model 연결~~
    SpModel sp = new SpModel();

    // 송편 목록 조회 (로그인한 사용자의 송편 목록)
    public List<SpModel> selectSpList(String spRecipient) {
        log.info("[Service][selectSpList] spList select success!!");

//        return spRepository.findAll();
        return spRepository.findBySpRecipient(spRecipient);
    }

    // 개별 송편 조회 (SP테이블 오브젝트아이디로)
    public SpModel selectSp(String spId) throws JsonProcessingException {
        log.info("[Service][selectSp] sp select success!!");

//        ObjectMapper objectMapper = new ObjectMapper();
        return spRepository.findBySpId(spId);
        //        return objectMapper.writeValueAsString(spRepository.findById(id));
    }

    // 송편 삭제
    public void removeSp(String spId) {
        spRepository.deleteBySpId(spId);
        log.info("[Service][removeSp] sp delete success!!");
    }

    // ----------------------- 지은 ----------------------
}
