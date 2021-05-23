package com.gcl.crm.service;

import com.gcl.crm.entity.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAllPermissions();
    Permission findById(Long id);
    List<Permission> findPermissionsByIdList(List<Long> idList);
}
