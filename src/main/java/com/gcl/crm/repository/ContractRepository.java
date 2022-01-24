package com.gcl.crm.repository;


import com.gcl.crm.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    @Query("select count(c.id) from Contract c")
    int countContractById();
    List<Contract> findAllByStatus(String status);
    List<Contract> findAllByBrokerCodeAndNumber(String brokerCode, String number);
    Contract findContractByContractNumber(String id);
    Contract findContractByContractNumberAndBrokerCodeAndNumber(String id, String brokerCode, String number);
}
