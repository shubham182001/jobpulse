package com.jobpulse.JobPulse.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobpulse.JobPulse.entities.Company;
import com.jobpulse.JobPulse.services.CompanyService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    public ResponseEntity<?> add(@RequestBody Company company) {
        try {
            Company saved = companyService.add(company);
            return ResponseEntity.status(200).body(Map.of("message", "Company Registered"));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Company>> getAll() {
    	List<Company> data = companyService.getAll();
    	HashMap<String, List<Company>> m=new HashMap<String, List<Company>>();
    	m.put("data", data);
        return ResponseEntity.ok(data);
    }
}