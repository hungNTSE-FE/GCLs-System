package com.gcl.crm.repository;

import com.gcl.crm.entity.MarketingGroup;
import com.gcl.crm.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketingGroupRepository extends JpaRepository<MarketingGroup, Long> {
    List<MarketingGroup> findAllByStatus(Status status);
}
