package com.moon.daltokki.Service;

import com.moon.daltokki.Model.SpModel;
import com.moon.daltokki.Repository.SpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SpService {

    @Autowired
    private SpRepository spRepository;

    public void saveSp(SpModel sp) {
        spRepository.save(sp);
    }

}
