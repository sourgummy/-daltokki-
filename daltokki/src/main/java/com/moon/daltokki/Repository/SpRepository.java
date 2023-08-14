package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.SpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpRepository extends MongoRepository<SpModel, String> {
    SpModel findByName(String name); // 작성자 이름으로 찾기
    SpModel findByNameAndSubject(String name, String subject); // 작성자 이름, 제목으로 찾기
    SpModel findBySpId(String spId);
    SpModel deleteBySpId(String spId);
}