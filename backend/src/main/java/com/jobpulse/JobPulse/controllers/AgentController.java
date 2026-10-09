package com.jobpulse.JobPulse.controllers;

import com.jobpulse.JobPulse.entities.Job;
import com.jobpulse.JobPulse.entities.repositories.JobRepo;
import com.jobpulse.JobPulse.services.JobPulseAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AgentController {

    private final JobPulseAgent agent;
    private final JobRepo jobRepo;

    @PostMapping("/agent/run")
    public ResponseEntity<Map<String, String>> runAgent(@RequestBody Map<String, String> body) {
        String query = body.getOrDefault("query", "find jobs for zoho");
        try {
            String result = agent.run(query);
            return ResponseEntity.ok(Map.of("result", result != null ? result : "No result"));
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("result", "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        return ResponseEntity.ok(jobRepo.findAll());
    }
}