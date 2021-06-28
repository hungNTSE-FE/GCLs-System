package com.gcl.crm.repository;

import com.gcl.crm.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceJpaRepository extends JpaRepository<Source, Long> {
    List<Source> findAllBySourceName(String name);
}
