package com.gcl.crm.service;

import com.gcl.crm.entity.Privilege;
import com.gcl.crm.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeService {

    @Autowired
    PrivilegeRepository privilegeRepository;

    public List<Privilege> getAllPrivileges(){
        return privilegeRepository.findAll();
    }
}
