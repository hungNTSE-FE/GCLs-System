package com.gcl.crm.service;

import com.gcl.crm.entity.*;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.PrivilegeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PrivilegeService {

    @Autowired
    PrivilegeRepository privilegeRepository;

    @Autowired
    RoleService roleService;

    @Autowired
    UserRoleService userRoleService;

    public List<Privilege> getAllPrivileges(){
        return privilegeRepository.findAllByStatus(Status.ACTIVE);
    }

    public List<Privilege> getPrivilegesByIdList(List<Long> idList){
        List<Privilege> privileges = privilegeRepository.findAllByStatus(Status.ACTIVE);
        List<Privilege> result = new ArrayList<>();
        for (Privilege privilege : privileges){
            if (idList.contains(privilege.getId())){
                result.add(privilege);
            }
        }
        return result;
    }

    public List<Privilege> getPrivilegesByModule(Module module){
        return privilegeRepository.findAllByStatusAndModule(Status.ACTIVE, module);
    }

    public List<Privilege> getUserPrivileges(User user){
        List<Privilege> privileges = new ArrayList<>();
        List<UserRole> userRoleList = userRoleService.getUserRoleByUser(user);
        List<Privilege> result = new ArrayList<>();
        for (UserRole userRole : userRoleList){
            privileges.addAll(userRole.getRole().getPrivileges());
        }
        for (Privilege privilege : privileges){
            if (result.contains(privilege)){
                continue;
            }
            result.add(privilege);
        }
        return result;
    }

}
