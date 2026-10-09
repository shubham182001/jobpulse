package com.jobpulse.JobPulse.entities.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobpulse.JobPulse.entities.Job;

public interface JobRepo extends JpaRepository<Job, UUID> {

    Optional<Job> findByUrl(String url);

}