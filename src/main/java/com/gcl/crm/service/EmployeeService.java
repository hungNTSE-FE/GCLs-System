package com.gcl.crm.service;

import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.MarketingGroup;
import com.gcl.crm.entity.User;
import com.gcl.crm.form.CreateEmployeeForm;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    List<Employee> getAllWorkingEmployees();
    List<Employee> getWorkingEmployeeByDepartment(Department department);
    List<Employee> getEmployeesByIdList(List<Long> aidList);
    boolean createEmployee(CreateEmployeeForm employeeForm, User currentUser, MultipartFile avatar) throws DuplicateKeyException;
    Employee getEmployeeById(Long id);
    boolean updateEmployee(CreateEmployeeForm employeeForm, MultipartFile avatar);
    boolean deleteEmployee(Long eid);
    List<Employee> getAllNotGroupedEmployees();
    List<Employee> getAllGroupedEmployees(String id);
    boolean isPhoneExisted(String phone, Long id);
    boolean isEmailExisted(String email, Long id);
    List<Employee> getAllWorkingEmployeesWithUserNotNull();
    Employee getEmployeeByEmail(String email);
}
