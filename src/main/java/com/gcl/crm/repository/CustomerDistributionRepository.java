package com.gcl.crm.repository;

import com.gcl.crm.entity.CustomerDistribution;
import com.gcl.crm.entity.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class CustomerDistributionRepository {

    @Autowired
    EntityManager entityManager;

    public void insertDataCustomer(CustomerDistribution customerDistribution) {
        entityManager.persist(customerDistribution);
    }

    public List<CustomerDistribution> getAll() {
        return entityManager.createQuery("from " + CustomerDistribution.class.getName())
                .getResultList();
    }
}
