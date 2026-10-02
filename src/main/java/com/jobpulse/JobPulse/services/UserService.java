package com.jobpulse.JobPulse.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobpulse.JobPulse.entities.User;
import com.jobpulse.JobPulse.entities.repositories.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	@Autowired
    private final UserRepo userRepo;
	@Autowired
    private final PasswordService passwordService;

    public User register(User user) {

        // Email already exists check
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered: " + user.getEmail());
        }
        user.setPassword(passwordService.hashPassword(user.getPassword()));
        user.setActive(true);
        return userRepo.save(user);
    }
}