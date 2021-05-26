package com.gcl.crm.service;

import com.gcl.crm.entity.Department;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    DepartmentRepository departmentRepository;

    @Override
    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Department findDepartmentById(Long id) {
        return departmentRepository.findById(id).get();
    }

    @Override
    public boolean createDepartment(Department department) {
        Department depart = departmentRepository.save(department);
        return depart != null;
    }

    @Override
    public boolean updateDepartment(Department department) {
        Department depart = departmentRepository.save(department);
        return depart != null;
    }

    @Override
    public boolean deleteDepartment(Long id) {
        Department department = departmentRepository.findById(id).get();
        department.setStatus(Status.INACTIVE);
        Department depart = departmentRepository.save(department);
        return depart != null;
    }
}
