package com.gcl.crm.repository;

import com.gcl.crm.entity.Level;
import com.gcl.crm.entity.Potential;
import com.gcl.crm.entity.Source;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PotentialRepository extends JpaRepository<Potential, Long> {
    Potential findPotentialByPhoneNumber(String phone);
    Potential findPotentialByEmail(String email);
    Potential findPotentialByIdAndAvailable(Long id, boolean available);
    Optional<Potential> findById(Long id);
    List<Potential> findAllByAvailable(boolean available);
    List<Potential> findAllByNameContainingAndPhoneNumberContainingAndEmailContainingAndSourceAndLevelAndAvailable
            (String name, String phone, String email, Source source, Level level, boolean available);
}
