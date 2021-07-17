package com.gcl.crm.repository;

import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.MarketingGroup;
import com.gcl.crm.entity.User;
import com.gcl.crm.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByStatusNot(EmployeeStatus status);
    List<Employee> findAllById(Long id);
    Optional<Employee> findByIdAndStatusNot(Long id, EmployeeStatus status);
    Employee findEmployeeByPhoneAndIdNot(String phone, Long id);
    Employee findEmployeeByCompanyEmailAndIdNot(String email, Long id);
    Employee findEmployeeByUser(User user);
    Employee findEmployeeByPhone(String phone);
    Employee findEmployeeByCompanyEmail(String email);
    Employee findEmployeeById(Long id);
}
