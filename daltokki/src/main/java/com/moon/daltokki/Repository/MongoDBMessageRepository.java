package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.MessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDBMessageRepository extends MongoRepository<MessageModel, String> {

}
