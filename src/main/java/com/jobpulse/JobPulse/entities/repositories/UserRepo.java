package com.jobpulse.JobPulse.entities.repositories;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.jobpulse.JobPulse.entities.User;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<User, UUID>
{
	  Optional<User> findByEmail(String email);

}
