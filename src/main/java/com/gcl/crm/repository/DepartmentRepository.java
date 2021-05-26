package com.gcl.crm.repository;

import com.gcl.crm.entity.Department;
import com.gcl.crm.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findAllByStatus(Status status);
}
