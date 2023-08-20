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
    private int sp_record;
    private String login_type;
    private String rabbit_type;
    //    private String social_login_id; // 사용자 id 는 username으로 통합


}
