package com.jobpulse.JobPulse.entities.repositories;

import com.jobpulse.JobPulse.entities.JobMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobMatchRepo extends JpaRepository<JobMatch, UUID> {
	
}