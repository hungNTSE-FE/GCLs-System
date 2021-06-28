package com.gcl.crm.repository;

import com.gcl.crm.entity.Level;
import com.gcl.crm.entity.Potential;
import com.gcl.crm.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PotentialRepository extends JpaRepository<Potential, Long> {
    Potential findPotentialByPhoneNumber(String phone);
    Potential findPotentialByEmail(String email);
    List<Potential> findAllByAvailable(boolean available);
    List<Potential> findAllByNameContainingAndPhoneNumberContainingAndEmailContainingAndSourceAndLevel
            (String name, String phone, String email, Source source, Level level);
}
