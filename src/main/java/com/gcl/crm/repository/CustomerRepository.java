package com.gcl.crm.repository;

import com.gcl.crm.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Repository
public class CustomerRepository {

    @Autowired
    EntityManager entityManager;

    public Customer register(Customer customer) {
        entityManager.persist(customer);
        return customer;
    }

    public void update(Customer customer) { entityManager.merge(customer); }

    public void updateCustomerByAccountNumber(String accountNumber, Integer levelId) {
        String sql = "update customer\n" +
                "set level_id = :level_id\n" +
                "where account_number = :account_number";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("account_number", accountNumber);
        query.setParameter("level_id", levelId);
        query.executeUpdate();
    }


}
