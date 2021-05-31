package com.gcl.crm.service;

import com.gcl.crm.entity.Permission;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.PermissionRepository;
import com.gcl.crm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAllPermissions() {
        return permissionRepository.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public Permission findPermissionById(Long id) {
        return permissionRepository.findPermissionByIdAndStatus(id, Status.ACTIVE);
    }

    @Override
    public boolean createPermission(Permission permission, List<Long> actionIds) {
        if (permission == null || permission.getId() == null){
            return false;
        }
        permission = permissionRepository.save(permission);
        return permission != null;
    }

}
