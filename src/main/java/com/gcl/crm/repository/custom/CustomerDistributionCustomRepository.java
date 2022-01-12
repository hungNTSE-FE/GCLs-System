package com.gcl.crm.repository.custom;

import com.gcl.crm.entity.Care;
import com.gcl.crm.entity.Customer;
import com.gcl.crm.entity.CustomerDistribution;
import com.gcl.crm.entity.MarketingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerDistributionCustomRepository extends JpaRepository<CustomerDistribution, Long> {
    List<CustomerDistribution> findAllByCustomerAndMarketingGroup(Customer customer, MarketingGroup marketingGroup);
}
