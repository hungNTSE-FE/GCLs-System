package com.gcl.crm.service;

import com.gcl.crm.entity.MarketingGroup;

import java.util.List;

public interface MarketingGroupService {
    MarketingGroup findMarketGroupById(String id);
    List<MarketingGroup> getAllMktByStatus();
    List<MarketingGroup> searchAllGroupMktByCode(MarketingGroup searchForm);
    boolean isCodeExisted(String code, Long id);
    boolean createMarketingGroup(MarketingGroup marketingGroup, List<Long> actionIds);
    boolean updateMarketingGroup(MarketingGroup marketingGroup, List<Long> actionIds);
    boolean deleteMarketingGroup(MarketingGroup marketingGroup);
}
