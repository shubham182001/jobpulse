package com.jobpulse.JobPulse.tool;

import java.util.List;
import java.util.Optional;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.jobpulse.JobPulse.dto.JobListing;
import com.jobpulse.JobPulse.entities.Company;
import com.jobpulse.JobPulse.entities.Job;
import com.jobpulse.JobPulse.entities.repositories.CompanyRepo;
import com.jobpulse.JobPulse.entities.repositories.JobRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JobExtractionTools {

	private final ChatClient chatClient;
	private final JobRepo jobRepo;
	private final CompanyRepo companyRepo;

	@Tool(description = "Extract job listings from career page text AND save them to the database. Returns list of extracted jobs.")
	public List<JobListing> extractJobs(@ToolParam(description = "Company name (e.g. 'Zoho')") String companyName,
			@ToolParam(description = "Plain text content of the career page") String pageText) {

		String truncated = pageText.length() > 4000 ? pageText.substring(0, 4000) : pageText;

		log.info("Extracting jobs from {} chars (truncated to {})", pageText.length(), truncated.length());

		try {
			List<JobListing> jobs = chatClient.prompt().user(u -> u.text("""
					      Extract all job listings from this career page text.
					      Return ONLY a JSON array. No markdown, no explanation.
					      Each job: {{"title": "...", "location": "...", "url": "...", "description": "..."}}
					      Include description from the source. If missing, use null.
					Truncate description to 200 chars.

					      If a field is missing, use null.
					      If no jobs found, return empty array [].

					      Page text:
					      ---
					      {pageText}
					      ---
					      """).param("pageText", truncated)).call()
					.entity(new ParameterizedTypeReference<List<JobListing>>() {
					});

			if (jobs == null || jobs.isEmpty()) {
				log.info("No jobs found");
				return List.of();
			}

			// Save each job directly here
			Optional<Company> companyOpt = companyRepo.findByName(companyName);
			if (companyOpt.isEmpty()) {
				log.warn("Company not found: {}", companyName);
				return jobs;
			}
			Company company = companyOpt.get();

			int saved = 0;
			for (JobListing j : jobs) {
				if (j.getTitle() == null || j.getTitle().isBlank() || j.getUrl() == null || j.getUrl().isBlank()) {
					log.warn("Skipping invalid job: {}", j.getTitle());
					continue;
				}
				if (jobRepo.findByUrl(j.getUrl()).isPresent()) {
					log.info("Job already exists: {}", j.getTitle());
					continue;
				}
				Job job = Job.builder().company(company).title(j.getTitle()).location(j.getLocation()).url(j.getUrl())
						.description(j.getDescription()).build();
				jobRepo.save(job);
				saved++;
				log.info("Saved job: {} @ {}", j.getTitle(), companyName);
			}

			log.info("Extracted {} jobs, saved {} new", jobs.size(), saved);
			return jobs;

		} catch (Exception e) {
			log.error("Extraction failed: {}", e.getMessage());
			return List.of();
		}
	}
}