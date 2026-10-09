package com.jobpulse.JobPulse.config;


import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jobpulse.JobPulse.entities.Company;
import com.jobpulse.JobPulse.entities.User;
import com.jobpulse.JobPulse.entities.UserPreference;
import com.jobpulse.JobPulse.entities.repositories.CompanyRepo;
import com.jobpulse.JobPulse.entities.repositories.UserPreferenceRepo;
import com.jobpulse.JobPulse.entities.repositories.UserRepo;
import com.jobpulse.JobPulse.services.PasswordService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepo userRepo;
    private final CompanyRepo companyRepo;
    private final UserPreferenceRepo userPreferenceRepo;
    private final PasswordService passwordService;

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) {
            log.info("Data already exists. Skipping seed.");
            return;
        }

        log.info("Seeding data...");

        // ===== USERS =====
        User shubham = createUser("Shubham", "Patil", "shubham@jobpulse.com", "pass123", "+919000000001");
        User priya = createUser("Priya", "Sharma", "priya@jobpulse.com", "pass123", "+919000000002");
        User rahul = createUser("Rahul", "Verma", "rahul@jobpulse.com", "pass123", "+919000000003");

        // ===== PREFERENCES =====
        createPreference(shubham,
                List.of("Java", "Spring Boot", "PostgreSQL", "Microservices"),
                List.of("Backend Developer", "Software Engineer"),
                List.of("Bangalore", "Remote"),
                new BigDecimal("1500000"), 3, "B.Tech Computer Science");

        createPreference(priya,
                List.of("Angular", "React", "TypeScript", "CSS"),
                List.of("Frontend Developer", "UI Engineer"),
                List.of("Pune", "Hyderabad", "Remote"),
                new BigDecimal("1200000"), 2, "B.E Information Technology");

        createPreference(rahul,
                List.of("Python", "Machine Learning", "TensorFlow", "PyTorch"),
                List.of("Data Scientist", "ML Engineer"),
                List.of("Bangalore", "Mumbai"),
                new BigDecimal("2000000"), 5, "M.Tech Artificial Intelligence");

        // ===== COMPANIES =====
        createCompany("Google", "https://careers.google.com/jobs/results/", null);
        createCompany("Stripe", "https://stripe.com/jobs", null);
        createCompany("Zoho", "https://www.zoho.com/careers/", null);
        createCompany("Freshworks", "https://www.freshworks.com/company/careers/", null);
        createCompany("Razorpay", "https://razorpay.com/jobs/", null);

        log.info("Seeding complete. Users: {}, Companies: {}", userRepo.count(), companyRepo.count());
    }

    private User createUser(String first, String last, String email, String password, String phone) {
        User user = User.builder()
                .firstName(first)
                .lastName(last)
                .email(email)
                .password(passwordService.hashPassword(password))
                .phoneNumber(phone)
                .active(true)
                .build();
        return userRepo.save(user);
    }

    private void createPreference(User user, List<String> skills, List<String> roles,
                                  List<String> locations, BigDecimal minSalary,
                                  int experience, String education) {
        UserPreference pref = UserPreference.builder()
                .user(user)
                .skills(skills)
                .preferredRoles(roles)
                .preferredLocations(locations)
                .minSalary(minSalary)
                .remoteOk(true)
                .experienceYears(experience)
                .education(education)
                .build();
        userPreferenceRepo.save(pref);
    }

    private void createCompany(String name, String careerUrl, String apiEndpoint) {
        Company company = Company.builder()
                .name(name)
                .careerUrl(careerUrl)
                .apiEndpoint(apiEndpoint)
                .isActive(true)
                .failureCount(0)
                .build();
        companyRepo.save(company);
    }
}