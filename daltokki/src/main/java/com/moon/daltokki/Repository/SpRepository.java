package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.SpModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SpRepository extends MongoRepository<SpModel, String> {

    List<SpModel> findBySpRecipient(String spRecipient);
    SpModel findBySpId(String spId);
    SpModel deleteBySpId(String spId);
}