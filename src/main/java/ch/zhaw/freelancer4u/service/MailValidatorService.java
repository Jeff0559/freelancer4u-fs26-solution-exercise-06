package ch.zhaw.freelancer4u.service;

import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import ch.zhaw.freelancer4u.model.MailInformation;

@Service
public class MailValidatorService {

    private static final Logger logger = LoggerFactory.getLogger(MailValidatorService.class);

    private final WebClient webClient;

    public MailValidatorService() {
        this.webClient = WebClient.builder()
            .baseUrl("https://disify.com")
            .defaultHeader("User-Agent", "Spring 5 WebClient")
            .filter(ServiceUtils.logRequest(logger))
            .build();
    }

    public MailInformation validateEmail(String email) {
        return webClient.get()
            .uri("/api/email/{email}", email)
            .retrieve()
            .bodyToMono(MailInformation.class)
            .timeout(Duration.ofSeconds(3))
            .block();
    }
}
