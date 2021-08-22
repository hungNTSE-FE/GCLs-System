package com.gcl.crm.service;

import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.MarketingGroup;
import com.gcl.crm.entity.UserRole;
import com.gcl.crm.enums.Status;
import com.gcl.crm.repository.MarketingGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Transactional
    public boolean createMarketingGroup(MarketingGroup marketingGroup, List<Long> actionIds) {
        marketingGroup.setStatus(Status.ACTIVE);
        marketingGroup.setCreateDate(getCurrentDate());
        marketingGroup = marketingGroupRepository.save(marketingGroup);
        List<Employee> employees = employeeService.getEmployeesByIdList(actionIds);
        for (Employee employee : employees){
            if (employee.getMarketingGroup() != null){
                continue;
            }
            employeeService.setGroupMkt(employee.getId(), marketingGroup);
        }
        return marketingGroup != null;
    }

    @Override
    @Transactional
    public boolean updateMarketingGroup(MarketingGroup marketingGroup, List<Long> employeeIdList) {
        marketingGroup.setLastModified(getCurrentDate());
        MarketingGroup mktGroup = marketingGroupRepository.save(marketingGroup);
        List<Employee> savedEmployees = marketingGroup.getEmployees();
        List<Employee> newEmployees = employeeService.getEmployeesByIdList(employeeIdList);
        for (Employee employee : savedEmployees){
            if (!employeeIdList.contains(employee.getId())){
                employeeService.setGroupMkt(employee.getId(), null);
            }
        }
        newEmployees.forEach(employee -> employeeService.setGroupMkt(employee.getId(), mktGroup));
        return mktGroup != null;
    }

    @Override
    public boolean deleteMarketingGroup(MarketingGroup marketingGroup) {
        if (marketingGroup == null) {
            return false;
        }
        marketingGroup.setLastModified(getCurrentDate());
        marketingGroup.setStatus(Status.INACTIVE);
        MarketingGroup market = marketingGroupRepository.save(marketingGroup);
        return market != null;
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
