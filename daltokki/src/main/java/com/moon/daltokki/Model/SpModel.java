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

    // _ 부호때문에 몽고디비 오류나서 카멜표기법으로 다 바꿨어요 - 지은
    private String spRecipient; // 받는사람
    private String spSender;    // 보낸사람 (닉네임)
    private String spSenderId; // 보낸사람 아이디
    private String spType; // 송편 속
    private String spColor; // 송편 색
    private String spContent; // 메시지 내용
}
