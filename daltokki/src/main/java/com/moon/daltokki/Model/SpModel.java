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

    private String name; // 받는 사람
    private String writer; // 쓰는 사람
    private String subject;
    private String content;
}
