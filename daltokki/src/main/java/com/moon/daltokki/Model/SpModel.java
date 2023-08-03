package com.moon.daltokki.Model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "sp")
public class SpModel {
    private String name;
    private String content;
}
