package com.gcl.crm.repository.custom;

import com.gcl.crm.entity.Customer;
import com.gcl.crm.entity.TradingAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByEmailAndPhoneNumberAndCustomerName(String email, String phoneNumber, String customerName);
    Customer findByEmailAndPhoneNumberAndCustomerName(String email, String phoneNumber, String customerName);
    Customer findByCustomerCode(String customerCode);
}
