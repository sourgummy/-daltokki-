package com.moon.daltokki.Service;

import com.moon.daltokki.Model.MessageModel;
import com.moon.daltokki.Repository.MongoDBMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MessageService {

    @Autowired
    private MongoDBMessageRepository mongoDBMessageRepository;

    public void saveMessage(MessageModel message) {
        mongoDBMessageRepository.save(message);
    }

}
