package com.gcl.crm.repository;

import com.gcl.crm.entity.CustomerDistribution;
import com.gcl.crm.entity.Source;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
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

    public boolean updateCusDisAfterRegistCus(Long potentialId, Integer cusId) {
        String sql = "update customer_distribution\n" +
                "set customer_id = :customer_id\n" +
                "where potential_id = :potential_id";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("potential_id", potentialId);
        query.setParameter("customer_id", cusId);
        return query.executeUpdate() > 0;
    }
}
