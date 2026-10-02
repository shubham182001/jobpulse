package com.jobpulse.JobPulse.entities;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ElementCollection
    @Builder.Default
    private List<String> skills = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<String> preferredRoles = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<String> preferredLocations = new ArrayList<>();

    private BigDecimal minSalary;

    @Builder.Default
    private Boolean remoteOk = true;

    private Integer experienceYears;
    
    private String education;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}