package com.moon.daltokki.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "user")
@ToString
public class UserModel {
    // private int idx;
    @Id
    private String id; // 몽고 db 자동 생성 id

    private String username;
    private String password;
    private String nickname;
    private String social_login_id;
    private String rabbit_type;
    private int sp_record;
    private String login_type;

}
