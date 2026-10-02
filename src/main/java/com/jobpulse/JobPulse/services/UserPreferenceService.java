package com.jobpulse.JobPulse.services;


import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jobpulse.JobPulse.entities.User;
import com.jobpulse.JobPulse.entities.UserPreference;
import com.jobpulse.JobPulse.entities.repositories.UserPreferenceRepo;
import com.jobpulse.JobPulse.entities.repositories.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    private final UserPreferenceRepo userPreferenceRepo;
    private final UserRepo userRepo;

    public UserPreference savePreference(UUID userId, UserPreference preference) {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));


        UserPreference existing = userPreferenceRepo.findByUserId(userId)
                .orElse(new UserPreference());

        existing.setUser(user);
        existing.setSkills(preference.getSkills());
        existing.setPreferredRoles(preference.getPreferredRoles());
        existing.setPreferredLocations(preference.getPreferredLocations());
        existing.setMinSalary(preference.getMinSalary());
        existing.setRemoteOk(preference.getRemoteOk());
        existing.setExperienceYears(preference.getExperienceYears());
        existing.setEducation(preference.getEducation());

        return userPreferenceRepo.save(existing);
    }

    public UserPreference getByUserId(UUID userId) {
        return userPreferenceRepo.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Preference not found for user: " + userId));
    }
}