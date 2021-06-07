package com.gcl.crm.repository;

import com.gcl.crm.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class MarketingRepository {

    @Autowired
    EntityManager entityManager;

//    private static final String sql = "";

    public List<Employee> a() {
        Query query = entityManager.createNativeQuery("select emp.* from employee emp inner join customer cus on emp.id = cus.id");
        List<Employee> a = query.getResultList();
        return null;
    }
}
