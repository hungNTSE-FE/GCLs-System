package com.gcl.crm.repository;

import com.gcl.crm.entity.Diary;
import com.gcl.crm.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    @Query("SELECT d FROM Diary AS d WHERE d.potential.id = ?1 ORDER BY d.createdDate ASC")
    List<Diary> findDiariesByPotentialId(Long potentialId);

    @Query(value = "SELECT d, e " +
            "FROM Employee AS e INNER JOIN Diary AS d " +
            "ON e.id = d.maker " +
            "WHERE d.potential.id = ?1 " +
            "ORDER BY d.createdDate ASC")
    List<Object[]> findDiaryListByPotentialId(Long potentialId);
}
