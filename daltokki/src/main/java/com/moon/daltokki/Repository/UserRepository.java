package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, String> {
    @Query("SELECT password FROM user WHERE username = :username")
    String selectPass(@Param("username") String username);

    Optional<UserModel> findByUsername(String username);
}
