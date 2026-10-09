package com.jobpulse.JobPulse.services;


import java.util.List;

import org.springframework.stereotype.Service;

import com.jobpulse.JobPulse.entities.Company;
import com.jobpulse.JobPulse.entities.repositories.CompanyRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepo companyRepo;

    public Company add(Company company) {
        if (companyRepo.findByName(company.getName()).isPresent()) {
            throw new RuntimeException("Company already exists: " + company.getName());
        }
        company.setIsActive(true);
        company.setFailureCount(0);
        return companyRepo.save(company);
    }

    public List<Company> getAll() {
        return companyRepo.findAll();
    }

    public List<Company> getActiveCompanies() {
        return companyRepo.findAll().stream()
                .filter(Company::getIsActive)
                .toList();
    }
}