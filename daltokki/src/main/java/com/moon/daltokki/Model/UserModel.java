package com.moon.daltokki.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "user")
public class UserModel {
    // private int idx;
    private String id;
    private String pw;
    private String nickname;
    private String social_login_id;
    private String rabbit_type;
    private int sp_record;
    private String login_type;
}
