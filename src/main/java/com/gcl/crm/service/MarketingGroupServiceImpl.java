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
    public MarketingGroup findMarketGroupById(String id) {
        Long marketingGroupId;
        try {
            marketingGroupId = Long.parseLong(id);
        } catch (NumberFormatException ex) {
            return null;
        }
        MarketingGroup marketingGroup = marketingGroupRepository.findByIdAndStatus(marketingGroupId, Status.ACTIVE);
        return marketingGroup;
    }

    @Override
    public boolean createMarketingGroup(MarketingGroup marketingGroup, List<Long> actionIds) {
        marketingGroup.setEmployees(employeeService.getEmployeesByIdList(actionIds));
        marketingGroup.setStatus(Status.ACTIVE);
        marketingGroup.setCreateDate(getCurrentDate());
        marketingGroup = marketingGroupRepository.save(marketingGroup);
        return marketingGroup != null;
    }

    @Override
    public boolean updateMarketingGroup(MarketingGroup marketingGroup) {
        if (marketingGroup.getId() == null) {
            return false;
        }
        MarketingGroup mktGroup = marketingGroupRepository.save(marketingGroup);
        return mktGroup != null;
    }

    @Override
    public List<MarketingGroup> getAllMktByStatus() {
        return marketingGroupRepository.findAllByStatus(Status.ACTIVE);
    }

    @Override
    public List<MarketingGroup> searchAllGroupMktByCode(MarketingGroup searchForm) {
        return marketingGroupRepository.findAllByStatusAndCodeContainingAndNameContaining(Status.ACTIVE, searchForm.getCode(), searchForm.getName());
    }

    @Override
    public boolean isCodeExisted(String code, Long id) {
        MarketingGroup marketingGroup = (id != null) ? marketingGroupRepository.findMarketingGroupByCodeAndIdNot(code, id)
                : marketingGroupRepository.findMarketingGroupByCode(code);
        return marketingGroup != null;
    }

    private Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
}
