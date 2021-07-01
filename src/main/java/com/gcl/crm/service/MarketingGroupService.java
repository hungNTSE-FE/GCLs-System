package com.gcl.crm.service;

import com.gcl.crm.entity.MarketingGroup;

import java.util.List;

public interface MarketingGroupService {
    boolean createMarketingGroup(MarketingGroup marketingGroup, List<Long> actionIds);
    List<MarketingGroup> getAllMktByStatus();
}
