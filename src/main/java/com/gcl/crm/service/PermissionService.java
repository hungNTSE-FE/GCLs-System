package com.gcl.crm.service;

import com.gcl.crm.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAllPermissions();
    Permission findPermissionById(Long id);
    boolean createPermission(Permission permission, List<Long> actionIds);
}
