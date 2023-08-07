package com.moon.daltokki.Model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "user")
@ToString
public class UserModel {
    // private int idx;
    private String user_id;
    private String user_pass;
    private String nickname;
    private String social_login_id;
    private String rabbit_type;
    private int sp_record;
    private String login_type;

}
