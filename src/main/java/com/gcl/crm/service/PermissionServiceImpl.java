package com.gcl.crm.service;

import com.gcl.crm.entity.Permission;
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
        return permissionRepository.findAllBy();
    }

    @Override
    public Permission findById(Long id) {
        return permissionRepository.findPermissionById(id);
    }

    @Override
    public List<Permission> findPermissionsByIdList(List<Long> idList) {
        List<Permission> permissions = null;
        if (idList != null) {
            permissions = new ArrayList<>();
            for (int i = 0; i < idList.size(); i++) {
                Permission permission = this.findById(idList.get(i));
                if (permission != null) {
                    permissions.add(permission);
                }
            }
        }
        return permissions;
    }
}
