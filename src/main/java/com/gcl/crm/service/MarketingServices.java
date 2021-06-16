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
    }

    public List<CustomerStatusForm> getListCustomerStatusReport(String fromDate, String toDate) {
        return marketingRepository.getCustomerStatusList(fromDate, toDate);
    }
}
