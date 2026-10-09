package com.jobpulse.JobPulse.entities.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.jobpulse.JobPulse.entities.Company;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepo extends JpaRepository<Company, UUID> {

    Optional<Company> findByName(String name);

}