package com.gcl.crm.service;

import com.gcl.crm.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAllPermission();
    Permission findById(Long id);
}
