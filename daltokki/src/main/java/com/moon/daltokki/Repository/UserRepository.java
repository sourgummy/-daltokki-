package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findById(String id);
}
