package com.gcl.crm.repository;

import com.gcl.crm.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
}
