package com.jobpulse.JobPulse.config;

import com.jobpulse.JobPulse.services.JobPulseAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ToolTestRunner implements CommandLineRunner {

    private final JobPulseAgent agent;

    @Override
    public void run(String... args) {
    	System.out.println("===== AGENT TEST =====");
    	String result = agent.run("find jobs for zoho");
    	System.out.println(result);
    	System.out.println("===== END =====");
    }
}