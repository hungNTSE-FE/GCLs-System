package com.gcl.crm.service;

import com.gcl.crm.repository.MarketingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MarketingServices {

    @Autowired
    MarketingRepository marketingRepository;

    public void initScreen() {
        marketingRepository.a();
    }

}
