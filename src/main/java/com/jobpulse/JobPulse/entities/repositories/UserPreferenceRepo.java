package com.jobpulse.JobPulse.entities.repositories;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jobpulse.JobPulse.entities.UserPreference;

public interface UserPreferenceRepo extends JpaRepository<UserPreference, UUID> {

    Optional<UserPreference> findByUserId(UUID userId);

}