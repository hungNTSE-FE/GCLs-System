package com.gcl.crm.repository;

import com.gcl.crm.entity.Employee;
import com.gcl.crm.entity.Level;
import com.gcl.crm.entity.Potential;
import com.gcl.crm.entity.Source;
import com.gcl.crm.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PotentialRepository extends JpaRepository<Potential, Long> {
    Potential findPotentialByPhoneNumberAndIdNot(String phone, Long id);
    Potential findPotentialByEmailAndIdNot(String email, Long id);
    Potential findPotentialByPhoneNumber(String phone);
    Potential findPotentialByEmail(String email);
    Potential findPotentialByIdAndStatus(Long id, Status status);
    Optional<Potential> findById(Long id);
    List<Potential> findAllByStatus(Status status);
    List<Potential> findAllByNameContainingAndPhoneNumberContainingAndEmailContainingAndStatus
            (String name, String phone, String email, Status status);
    List<Potential> findAllByDateContaining(String date);
    List<Potential> findAllByDate(String date);
}
