package com.gcl.crm.service;

import com.gcl.crm.entity.Permission;
import com.gcl.crm.repository.PermissionRepository;
import com.gcl.crm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAllPermission() {
        return permissionRepository.findAllBy();
    }

    @Override
    public Permission findById(Long id) {
        return permissionRepository.findPermissionById(id);
    }
}
