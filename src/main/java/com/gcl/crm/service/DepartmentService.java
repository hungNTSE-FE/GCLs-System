package com.gcl.crm.service;

import com.gcl.crm.entity.Company;
import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Employee;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartmentService {
    List<Department> findAllDepartments();
    Department findDepartmentById(String id);
    boolean createDepartment(Department department);
    boolean updateDepartment(Department department);
    boolean deleteDepartment(String id);
    boolean isNameExisted(String name, Long id);
    List<Department> findDepartmentsByCompany(Company company);
    List<Department> findDepartmentsByIdList(List<Long> idList);
    List<Department> findDepartmentsByTask(Long departmentID, List<Employee> employees);
    List<Department> findAllDepartmentsWithEmployees();
}
