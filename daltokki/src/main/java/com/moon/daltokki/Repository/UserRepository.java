package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, String> {
    @Query("SELECT user_pass FROM user WHERE user_id = :user_id")
    String selectPass(@Param("user_id") String user_id);
}
