package com.jobpulse.JobPulse.tool;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobScrapingTools {

    @Tool(description = "Fetch content from a company career page or API URL. Works for both HTML pages and JSON APIs. Returns plain text or JSON content.")
    public String fetchCareerPage(
            @ToolParam(description = "Full URL of career page or API endpoint") String url) {
        try {
            log.info("Fetching: {}", url);

            // Fetch raw response as string (works for both HTML and JSON)
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (compatible; JobPulse/1.0)")
                    .ignoreContentType(true)   // ← important: allows JSON
                    .timeout(15000)
                    .get();

            // Get full body text (works for JSON too, since JSON is text)
            String content = doc.body().text();

            log.info("Fetched {} characters from {}", content.length(), url);
            return content;

        } catch (Exception e) {
            log.error("Failed to fetch {}: {}", url, e.getMessage());
            return "ERROR fetching " + url + ": " + e.getMessage();
        }
    }
}