package com.gcl.crm.repository;

import com.gcl.crm.entity.Identification;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IdentificationRepository extends JpaRepository<Identification, Long> {
    Identification findByIdentityNumber(String identityNumber);
}
