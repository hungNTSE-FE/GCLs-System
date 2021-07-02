package com.gcl.crm.service;

import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.MarketingGroup;

import java.util.List;

public interface EmployeeService {
    List<Employee> getAllEmployees();
    List<Employee> getAllWorkingEmployees();
    List<Employee> getEmployeesByIdList(List<Long> aidList);
    boolean createEmployee(Employee employee, Long pid, Long did);
    Employee getEmployeeById(Long id);
    boolean updateEmployee(Employee employee, Long pid, Long did, String userName, String password);
    boolean deleteEmployee(Long eid);
    List<Employee> getAllNotGroupedEmployees();
    List<Employee> getAllGroupedEmployees();
}
