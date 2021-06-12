package com.gcl.crm.service;

import com.gcl.crm.entity.Customer;
import com.gcl.crm.form.CustomerForm;
import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.form.CustomerStatusReportForm;
import com.gcl.crm.repository.CustomerRepository;
import com.gcl.crm.repository.MarketingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MarketingServices {

    @Autowired
    MarketingRepository marketingRepository;

    @Autowired
    CustomerRepository customerRepository;

    public void initScreen() {
        List<CustomerStatusForm> customerStatusFormList = marketingRepository.getCustomerStatusList();
        List<CustomerStatusReportForm> customerStatusReportFormList
                = marketingRepository.getCustomerStatusReportList("2021/01/01", "2021/06/25");
    }

    @Transactional
    public void registerCustomer(CustomerForm customerForm) {
        Customer customer = convertToCustomerEntity(customerForm);
        customerRepository.save(customer);
    }

    private Customer convertToCustomerEntity(CustomerForm customerForm) {
        Customer customer = new Customer();
        customer.setCustomerName(customerForm.getCustomerName());
        customer.setAddress(customerForm.getAddress());
        customer.setEmail(customerForm.getEmail());
        customer.setPhoneNumber(customerForm.getPhoneNumber());

        return customer;
    }

}
