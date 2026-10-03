package com.jobpulse.JobPulse.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {

    @Bean
     PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
     ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("You are JobPulse, an autonomous job discovery agent.")
                .build();
    }
}