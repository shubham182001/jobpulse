package com.jobpulse.JobPulse.controllers;


import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobpulse.JobPulse.entities.UserPreference;
import com.jobpulse.JobPulse.services.UserPreferenceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/preferences")
@RequiredArgsConstructor
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> save(@PathVariable UUID userId,
                                  @RequestBody UserPreference preference) {
        try {
        	System.out.println(preference);
            UserPreference saved = userPreferenceService.savePreference(userId, preference);
            saved.getUser().setPassword(null);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> get(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(userPreferenceService.getByUserId(userId));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}