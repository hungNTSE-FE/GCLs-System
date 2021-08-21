package com.gcl.crm.service;

import com.gcl.crm.entity.*;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PrivilegeService privilegeService;

    @Autowired
    UserService userService;

    @Autowired
    UserRoleService userRoleService;

    public List<Role> getAllActiveRoles(){
        return roleRepository.findAllByStatus(Status.ACTIVE);
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public List<Role> search(String keyword){
        return roleRepository.findAllByNameContaining(keyword);
    }

    public Role getRoleById(Long roleId){
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent()){
            return role.get();
        }
        return null;
    }


    public boolean createRole(Role role, List<Long> privilegeIdList, User user){
        List<Privilege> privileges = privilegeService.getPrivilegesByIdList(privilegeIdList);
        role.setPrivileges(privileges);
        role.setStatus(Status.ACTIVE);
        role.setCreator(user.getEmployee().getId());
        role.setCreatedDate(this.getCurrentDate());
        roleRepository.save(role);
        return true;
    }

    public boolean updateRole(Role newRole, List<Long> privilegeIdList, User user){
        Optional<Role> role = roleRepository.findByIdAndStatus(newRole.getId(), Status.ACTIVE);
        if (!role.isPresent()){
            return false;
        }
        List<Privilege> privileges = privilegeService.getPrivilegesByIdList(privilegeIdList);
        role.get().setName(newRole.getName());
        role.get().setDescription(newRole.getDescription());
        role.get().setPrivileges(privileges);
        role.get().setLastModified(this.getCurrentDate());
        role.get().setLastModifier(user.getEmployee().getId());
        roleRepository.save(role.get());
        return true;
    }

    public boolean enableRole(Long roleId, User user){
        Optional<Role> role = roleRepository.findByIdAndStatus(roleId, Status.INACTIVE);
        if (!role.isPresent()){
            return false;
        }
        role.get().setStatus(Status.ACTIVE);
        role.get().setLastModifier(user.getEmployee().getId());
        role.get().setLastModified(this.getCurrentDate());
        roleRepository.save(role.get());
        return true;
    }

    public boolean disableRole(Long roleId, User user){
        Optional<Role> role = roleRepository.findByIdAndStatus(roleId, Status.ACTIVE);
        if (!role.isPresent()){
            return false;
        }
        role.get().setStatus(Status.INACTIVE);
        role.get().setLastModifier(user.getEmployee().getId());
        role.get().setLastModified(this.getCurrentDate());
        roleRepository.save(role.get());
        return true;
    }

    @Transactional
    public boolean decentralizeRole(Long roleId, List<Long> userIdList, User currentUser){
        Optional<Role> role = roleRepository.findByIdAndStatus(roleId, Status.ACTIVE);
        if (!role.isPresent()){
            return false;
        }
        List<UserRole> finalUserRoleList = new ArrayList<>();
        List<UserRole> savedUserRoleList = userRoleService.getUserRoleByRole(role.get());
        for (UserRole userRole : savedUserRoleList){
            if (!userIdList.contains(userRole.getUser().getUserId())){
                userRole.setStatus(Status.INACTIVE);
                finalUserRoleList.add(userRole);
                continue;
            }
            userIdList.remove(userRole.getUser().getUserId());
        }
        List<User> users = userService.getUsersByIdList(userIdList);
        for (User user : users){
            UserRole userRole = new UserRole();
            userRole.setRole(role.get());
            userRole.setUser(user);
            userRole.setCreator(currentUser.getEmployee().getId());
            userRole.setStatus(Status.ACTIVE);
            userRole.setCreatedDate(this.getCurrentDate());
            finalUserRoleList.add(userRole);
        }
        role.get().setUserRoles(finalUserRoleList);
        roleRepository.save(role.get());
        return true;
    }

    private Date getCurrentDate(){
        return new Date(System.currentTimeMillis());
    }
}
