package com.gcl.crm.repository.custom;

import com.gcl.crm.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByEmailOrPhoneNumber(String email, String phoneNumber);

    Customer findByEmailOrPhoneNumber(String email, String phoneNumber);
}
