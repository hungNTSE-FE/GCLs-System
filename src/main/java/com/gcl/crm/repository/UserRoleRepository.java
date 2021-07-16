package com.gcl.crm.repository;

import com.gcl.crm.entity.Role;
import com.gcl.crm.entity.User;
import com.gcl.crm.entity.UserRole;
import com.gcl.crm.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByRoleAndStatus(Role role, Status status);
    List<UserRole> findAllByUserAndStatus(User user, Status status);
}
