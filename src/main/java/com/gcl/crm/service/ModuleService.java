package com.gcl.crm.service;

import com.gcl.crm.entity.Module;
import com.gcl.crm.repository.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    public List<Module> getAllModules(){
        return moduleRepository.findAll();
    }
}
