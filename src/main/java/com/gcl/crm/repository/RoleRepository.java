package com.gcl.crm.repository;

import com.gcl.crm.entity.Role;
import com.gcl.crm.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAllByStatus(Status status);
    Optional<Role> findByIdAndStatus(Long id, Status status);
}
