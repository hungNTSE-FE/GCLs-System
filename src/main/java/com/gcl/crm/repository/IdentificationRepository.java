package com.gcl.crm.repository;

import com.gcl.crm.entity.Identification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IdentificationRepository extends JpaRepository<Identification, String> {
    @Override
    Optional<Identification> findById(String s);
}
