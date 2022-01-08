package com.gcl.crm.repository;

import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.MarketingGroup;
import com.gcl.crm.entity.User;
import com.gcl.crm.enums.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByStatusNot(EmployeeStatus status);
    List<Employee> findAllById(Long id);
    List<Employee> findAllByPhoneOrCompanyEmail(String phone, String companyEmail);
    Optional<Employee> findByIdAndStatusNot(Long id, EmployeeStatus status);
    Employee findEmployeeByPhoneAndIdNot(String phone, Long id);
    Employee findEmployeeByCompanyEmailAndIdNot(String email, Long id);
    Employee findEmployeeByPhone(String phone);
    Employee findEmployeeByCompanyEmail(String email);

    @Query("SELECT e FROM Employee AS e WHERE e.status = ?1 AND e.department.id = ?2")
    List<Employee> getEmployeesByStatusAndDepartmentId(EmployeeStatus status, Long departmentId);

    @Modifying
    @Query("UPDATE Employee AS e SET e.marketingGroup = ?2 WHERE e.id = ?1")
    void setGroupMkt(Long employeeId, MarketingGroup marketingGroup);
}
