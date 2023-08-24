package com.moon.daltokki.Service;

import com.moon.daltokki.Model.SpModel;
import com.moon.daltokki.Model.UserModel;
import com.moon.daltokki.Repository.SpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
public class SpService {

    @Autowired
    private SpRepository spRepository;

    public void saveSp(SpModel sp) {
        spRepository.save(sp);
    }

    // (진행중) 해당 user의 송편을 작성한 발신자 닉네임 중복여부 체크
    public boolean isSenderDuplicate(String spSender) {
        log.info("[SpService][isSenderDuplicate] 넘어온 닉네임 : {}", spSender);
        return spRepository.existsBySpSender(spSender);
    }
}
