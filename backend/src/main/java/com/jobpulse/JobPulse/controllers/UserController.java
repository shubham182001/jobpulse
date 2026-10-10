package com.jobpulse.JobPulse.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobpulse.JobPulse.entities.User;
import com.jobpulse.JobPulse.entities.repositories.UserRepo;
import com.jobpulse.JobPulse.services.PasswordService;
import com.jobpulse.JobPulse.services.UserService;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

	@Autowired
	private final UserService userService;
	private final UserRepo userRepo; 
	private final PasswordService passwordService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user) {
		try {
			User saved = userService.register(user);
			saved.setPassword(null);
			return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "Registration Successfull"));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
		try {
			String email = body.get("email");
			String password = body.get("password");

			User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

			if (password == null || !passwordService.matches(password, user.getPassword())) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid password"));
			}

			Map<String, Object> response = new HashMap<>();
			user.setPassword(null);
			response.put("success", true);
			response.put("user", user);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of("data", response));

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
		}
	}
}