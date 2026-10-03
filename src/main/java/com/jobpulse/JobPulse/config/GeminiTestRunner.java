package com.jobpulse.JobPulse.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GeminiTestRunner implements CommandLineRunner {

	@Autowired
    private ChatClient chatClient; 

    @Override
    public void run(String... args) {
        System.out.println("===== GEMINI TEST =====");
        try {
//            String response = chatClient.prompt()
//                    .user("Hello, tell me your role name just???")
//                    .call()
//                    .content();
//            System.out.println("Gemini says: " + response);
        	System.out.println("test is Skipped");
        } catch (Exception e) {
            System.out.println("GEMINI ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("===== TEST END =====");
    }
}