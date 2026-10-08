package com.jobpulse.JobPulse.tool;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.jobpulse.JobPulse.entities.Company;
import com.jobpulse.JobPulse.entities.repositories.CompanyRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompanyTools {

    private final CompanyRepo companyRepo;

    @Tool(description = "Get the list of all active companies that JobPulse monitors. Returns company name, career URL, and API endpoint (if available). Use this first to know which companies to check.")
    public List<Company> getActiveCompanies() {
        List<Company> companies = companyRepo.findAll().stream()
                .filter(Company::getIsActive)
                .toList();
        log.info("Returning {} active companies", companies.size());
        return companies;
    }
}