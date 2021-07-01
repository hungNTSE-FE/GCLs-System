package com.gcl.crm.service;

import com.gcl.crm.entity.MarketingGroup;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.MarketingGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class MarketingGroupServiceImpl implements MarketingGroupService{

    @Autowired
    MarketingGroupRepository marketingGroupRepository;

    @Autowired
    EmployeeService employeeService;

    @Override
    public boolean createMarketingGroup(MarketingGroup marketingGroup, List<Long> actionIds) {
        marketingGroup.setEmployees(employeeService.getEmployeesByIdList(actionIds));
        marketingGroup.setStatus(Status.ACTIVE);
        marketingGroup.setCreateDate(getCurrentDate());
        marketingGroup = marketingGroupRepository.save(marketingGroup);
        return marketingGroup != null;
    }

    @Override
    public List<MarketingGroup> getAllMktByStatus() {
        return marketingGroupRepository.findAllByStatus(Status.ACTIVE);
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
