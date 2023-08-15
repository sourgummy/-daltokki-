package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.SpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpRepository extends MongoRepository<SpModel, String> {

    SpModel findBySpId(String spId);
    SpModel deleteBySpId(String spId);
}