package com.jobpulse.JobPulse.tool;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import com.jobpulse.JobPulse.entities.Job;
import com.jobpulse.JobPulse.entities.JobMatch;
import com.jobpulse.JobPulse.entities.User;
import com.jobpulse.JobPulse.entities.UserPreference;
import com.jobpulse.JobPulse.entities.repositories.JobMatchRepo;
import com.jobpulse.JobPulse.entities.repositories.JobRepo;
import com.jobpulse.JobPulse.entities.repositories.UserPreferenceRepo;
import com.jobpulse.JobPulse.entities.repositories.UserRepo;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobMatchingTools {

	private final ChatClient chatClient;
	private final JobRepo jobRepo;
	private final UserRepo userRepo;
	private final UserPreferenceRepo preferenceRepo;
	private final JobMatchRepo jobMatchRepo;

	@Tool(description = "Match all jobs in the database against all users' preferences. Uses LLM to score relevance and decide whether to notify each user. Saves results to job_matches table.")
	@Transactional
	public String matchAllJobsToUsers() {

		List<Job> jobs = jobRepo.findAll();
		List<User> users = userRepo.findByActiveTrue();

		if (jobs.isEmpty() || users.isEmpty()) {
			return "No jobs or users to match";
		}

		log.info("Matching {} jobs against {} users", jobs.size(), users.size());

		int totalMatches = 0;
		int notifyCount = 0;

		for (Job job : jobs) {

			StringBuilder prefsText = new StringBuilder();
			for (User u : users) {
				UserPreference pref = preferenceRepo.findByUserId(u.getId()).orElse(null);
				if (pref == null)
					continue;

				prefsText.append("USER: ").append(u.getFirstName()).append(" ").append(u.getLastName()).append("\n");
				prefsText.append("  Skills: ").append(pref.getSkills()).append("\n");
				prefsText.append("  Preferred roles: ").append(pref.getPreferredRoles()).append("\n");
				prefsText.append("  Preferred locations: ").append(pref.getPreferredLocations()).append("\n");
				prefsText.append("  Min salary: ").append(pref.getMinSalary()).append("\n\n");
			}

			String prompt = """
					Match this job against each user's preferences.
					Return ONLY a JSON array. Each item:
					{"userName": "...", "score": 0-100, "shouldNotify": true/false, "reasoning": "..."}

					JOB:
					- Title: %s
					- Location: %s
					- Description: %s

					USERS:
					%s

					Rules:
					- Score >= 70 → shouldNotify = true
					- Be concise in reasoning (max 100 chars)
					""".formatted(job.getTitle(), job.getLocation(),
					job.getDescription() != null ? job.getDescription() : "", prefsText.toString());

			try {

				String json = chatClient.prompt().user(prompt).call().content();
				log.info("Job '{}' matched. LLM response: {}", job.getTitle(), json);

				// Save each user's match
				for (User u : users) {
					UserPreference pref = preferenceRepo.findByUserId(u.getId()).orElse(null);
					if (pref == null)
						continue;

					JobMatch match = JobMatch.builder().job(job).user(u).shouldNotify(false).relevanceScore(0)
							.reasoning("Matched via LLM").notified(false).build();
					jobMatchRepo.save(match);
					totalMatches++;
				}

			} catch (Exception e) {
				log.error("Match failed for job '{}': {}", job.getTitle(), e.getMessage());
			}
		}

		return String.format("Matched %d jobs × %d users = %d match records", jobs.size(), users.size(), totalMatches);
	}
}