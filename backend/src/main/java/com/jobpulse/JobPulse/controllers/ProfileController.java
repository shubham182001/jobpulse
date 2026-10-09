package com.jobpulse.JobPulse.controllers;

import com.jobpulse.JobPulse.entities.User;
import com.jobpulse.JobPulse.entities.UserPreference;
import com.jobpulse.JobPulse.entities.repositories.UserRepo;
import com.jobpulse.JobPulse.entities.repositories.UserPreferenceRepo;
import com.jobpulse.JobPulse.services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepo userRepo;
    private final UserPreferenceRepo preferenceRepo;
    private final PasswordService passwordService;

    public record ProfileRequest(String firstName, String lastName, String email, String password, String phoneNumber,
            List<String> skills, List<String> preferredRoles, List<String> preferredLocations, Double minSalary,
            Boolean remoteOk, Integer experienceYears, String education) {
    }

    // ===== CREATE =====
    @PostMapping("/create")
    public ResponseEntity<?> createProfile(@RequestBody ProfileRequest req) {
        try {
            if (userRepo.findByEmail(req.email()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Email already registered"));
            }

            User user = User.builder()
                    .firstName(req.firstName())
                    .lastName(req.lastName())
                    .email(req.email())
                    .password(passwordService.hashPassword(req.password() != null ? req.password() : "default123"))
                    .phoneNumber(req.phoneNumber())
                    .active(true)
                    .build();
            userRepo.save(user);

            UserPreference pref = UserPreference.builder()
                    .user(user)
                    .skills(req.skills() != null ? req.skills() : new ArrayList<>())
                    .preferredRoles(req.preferredRoles() != null ? req.preferredRoles() : new ArrayList<>())
                    .preferredLocations(req.preferredLocations() != null ? req.preferredLocations() : new ArrayList<>())
                    .minSalary(req.minSalary() != null ? java.math.BigDecimal.valueOf(req.minSalary()) : null)
                    .remoteOk(req.remoteOk() != null ? req.remoteOk() : true)
                    .experienceYears(req.experienceYears())
                    .education(req.education())
                    .build();
            preferenceRepo.save(pref);

            return ResponseEntity.ok(Map.of(
                    "message", "Profile created",
                    "userId", user.getId(),
                    "email", user.getEmail()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== GET BY ID =====
    @PostMapping("/getbyid")
    public ResponseEntity<?> getById(@RequestBody Map<String, String> body) {
        try {
            UUID userId = UUID.fromString(body.get("userId"));
            User user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserPreference pref = preferenceRepo.findByUserId(userId).orElse(null);

            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("preference", pref);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== GET BY EMAIL =====
    @PostMapping("/getbyemail")
    public ResponseEntity<?> getByEmail(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            if(email==null || email.equals("") || email.isEmpty()==true)
            {
            	 return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                         .body(Map.of("error", "Email cannot be empty"));
            }
            User user = userRepo.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            UserPreference pref = preferenceRepo.findByUserId(user.getId()).orElse(null);

            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("preference", pref);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    // ===== GET ALL =====
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllProfiles() {
        return ResponseEntity.ok(userRepo.findAll());
    }
}