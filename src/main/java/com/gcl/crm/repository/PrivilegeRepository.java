package com.gcl.crm.repository;

import com.gcl.crm.entity.Module;
import com.gcl.crm.entity.Privilege;
import com.gcl.crm.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    List<Privilege> findAllByStatusAndModule(Status status, Module module);
    Optional<Privilege> findByIdAndStatus(Long id, Status status);
    List<Privilege> findAllByStatus(Status status);
}
