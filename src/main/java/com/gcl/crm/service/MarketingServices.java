package com.gcl.crm.service;

import com.gcl.crm.form.CustomerStatusForm;
import com.gcl.crm.form.CustomerStatusReportForm;
import com.gcl.crm.repository.MarketingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarketingServices {

    @Autowired
    MarketingRepository marketingRepository;

    public void initScreen() {
        List<CustomerStatusForm> customerStatusFormList = marketingRepository.getCustomerStatusList();
        List<CustomerStatusReportForm> customerStatusReportFormList
                = marketingRepository.getCustomerStatusReportList("2021/01/01", "2021/06/25");
    }

}
