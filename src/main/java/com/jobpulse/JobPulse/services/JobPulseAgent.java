package com.jobpulse.JobPulse.services;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.jobpulse.JobPulse.tool.CompanyTools;
import com.jobpulse.JobPulse.tool.JobExtractionTools;
import com.jobpulse.JobPulse.tool.JobMatchingTools;
import com.jobpulse.JobPulse.tool.JobScrapingTools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobPulseAgent {

	private final ChatClient.Builder chatClientBuilder;
	private final JobScrapingTools scrapingTools;
	private final JobExtractionTools extractionTools;
	private final CompanyTools companyTools;
	private final JobMatchingTools jobMatchingTools; 

	public String run(String userQuery) {
		log.info("Agent run: {}", userQuery);

		ChatClient client = chatClientBuilder.defaultSystem("""
				        You are JobPulse, an autonomous job discovery agent.

				        GOAL:
				        Find jobs from companies in the database and report them.
						 - Process ONLY Zoho.
				        CONSTRAINTS:
				        - LLM API rate limit: 8000 tokens per minute
				        - Large content (over 5000 chars) is expensive and may hit rate limits
				        - Each tool call costs tokens
				        - You have limited time and resources

				        USE AVAILABLE TOOLS:

				        YOUR TASK:
				        - Decide which companies to check based on efficiency (or Better whose have api-endpoint)
				        - You don't have to check all companies
				        - Prioritize companies that are likely to give good results
				          without exceeding rate limits
				        - Reason about your strategy before acting
				        - Report which companies you checked and which you skipped,
				          along with your reasoning

				        Be autonomous. Think strategically. Be concise.
				        After extracting jobs with extractJobs, call saveJob for
						each job to persist it to the database.
						Report how many jobs were saved vs already existed
						After saving jobs, please to match jobs against users and save it in that DB.
				        """).defaultTools(scrapingTools, extractionTools, companyTools, jobMatchingTools).build();

		return client.prompt().user(userQuery).call().content();
	}
}