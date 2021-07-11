package com.gcl.crm.repository;

import com.gcl.crm.entity.Department;
import com.gcl.crm.entity.Level;
import com.gcl.crm.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LevelRepository extends JpaRepository<Level, Integer> {
    Level findByLevelId(Integer id);
}
