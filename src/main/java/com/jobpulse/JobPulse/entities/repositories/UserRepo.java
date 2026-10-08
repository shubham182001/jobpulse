package com.jobpulse.JobPulse.entities.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jobpulse.JobPulse.entities.User;


@Repository
public interface UserRepo extends JpaRepository<User, UUID>
{
	  Optional<User> findByEmail(String email);
	  List<User> findByActiveTrue();  

}
