package com.gcl.crm.service;

import com.gcl.crm.entity.Level;
import com.gcl.crm.repository.LevelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LevelService {

    @Autowired
    LevelRepository levelRepository;

    public List<Level> getAll(){
        return levelRepository.findAll();
    }

    public Level getLevelById(Integer id){
        Optional<Level> level = levelRepository.findById(id);
        if (level.isPresent()){
            return level.get();
        }
        return null;
    }
}
