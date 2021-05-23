package com.gcl.crm.repository;

import com.gcl.crm.entity.Department;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>,
        PagingAndSortingRepository<Department, Long> {
    List<Department> findAllBy(Pageable pageable);
    Department findDepartmentById(Long id);
    List<Department> findAllByNameContaining(String keyword, Pageable pageable);
    int countAllByNameContaining(String keyword);
    Department save(Department department);
}
