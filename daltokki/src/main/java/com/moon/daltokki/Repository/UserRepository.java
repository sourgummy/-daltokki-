package com.moon.daltokki.Repository;

import com.moon.daltokki.Model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, String> {
    /*@Query("SELECT password FROM user WHERE username = :username")
    String selectPass(@Param("username") String username);*/

    Optional<UserModel> findByusername(String username);
    UserModel findByEmail(String email);

    // 해당 아이디/이메일이 DB에 존재하는지 확인 - 지은
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
