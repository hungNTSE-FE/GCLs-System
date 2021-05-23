package com.gcl.crm.service;

import com.gcl.crm.entity.Department;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    List<Department> findAllDepartments(Pageable pageable);
    Department findDepartmentById(Long id);
    List<Department> findDepartmentsByName(String keyword, Pageable pageable);
    List<Integer> getPageList(int pageSize, String keyword);
    void createDepartment(Department department);
}
