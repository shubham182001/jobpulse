package com.jobpulse.JobPulse.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobListing {
	private String title;
	private String location;
	private String url;
	private String description;
}