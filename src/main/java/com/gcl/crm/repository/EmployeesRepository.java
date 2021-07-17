package com.gcl.crm.repository;

import com.gcl.crm.entity.Employee;
import com.gcl.crm.form.EmployeeSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class EmployeesRepository {

    @Autowired
    EntityManager entityManager;

    public List<EmployeeSearchForm> getListEmployeeByDepartmentId(Long id) {
        String sql = "SELECT emp.id, emp.full_name as name\n" +
                    "FROM employee emp\n" +
                    "INNER JOIN department dep\n" +
                    "ON emp.department_id = dep.id\n" +
                    "WHERE dep.id = :department_id";
        Query query = entityManager.createNativeQuery(sql, "getEmployeesByDepartmentId");
        query.setParameter("department_id", id);
        return query.getResultList();
    }
}
