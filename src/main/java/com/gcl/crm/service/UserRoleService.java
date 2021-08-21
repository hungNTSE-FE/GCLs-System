package com.gcl.crm.service;

import com.gcl.crm.entity.Role;
import com.gcl.crm.entity.User;
import com.gcl.crm.entity.UserRole;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.RoleRepository;
import com.gcl.crm.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    UserRoleRepository userRoleRepository;

    @Autowired
    RoleRepository roleRepository;

    public List<UserRole> getUserRoleByRole(Role role){
        return userRoleRepository.findAllByRoleAndStatus(role, Status.ACTIVE);
    }

    public List<UserRole> getUserRoleByUser(User user){
        List<UserRole> userRoles = userRoleRepository.findAllByUserAndStatus(user, Status.ACTIVE);
        List<UserRole> result = new ArrayList<>();
        for (UserRole userRole : userRoles){
            if (userRole.getRole().getStatus().equals(Status.ACTIVE)) {
                result.add(userRole);
            }
        }
        return result;
    }
}
