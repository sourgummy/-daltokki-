package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.SpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpRepository extends MongoRepository<SpModel, String> {

    List<SpModel> findBySpRecipient(String spRecipient);
    SpModel findBySpId(String spId);
    SpModel deleteBySpId(String spId);

    // (진행중) 해당 user의 송편을 작성한 발신자 닉네임 중복여부 체크
    boolean existsBySpSender(String spSender);
}