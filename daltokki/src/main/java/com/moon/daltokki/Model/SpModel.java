package com.moon.daltokki.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "sp")
public class SpModel {
    @Id
    private String spId; // 오브젝트 아이디

    private String sp_recipient; // 받는사람
    private String sp_sender;    // 보낸사람 (닉네임)
    private String sp_sender_id; // 보낸사람 아이디
    private String sp_type; // 송편 속
    private String sp_color; // 송편 색
    private String sp_content; // 메시지 내용
}
