package ch.zhaw.freelancer4u.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.CompanyService;

@RestController
@RequestMapping("/api/random")
public class CreateRandomJobsController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;

    private static final String[] TITLES = {
            "Build a REST API", "Write unit tests", "Design a database schema",
            "Implement authentication", "Create a dashboard", "Refactor legacy code"
    };

    private static final String[] DESCRIPTIONS = {
            "Build a RESTful API for a blog application",
            "Write unit tests for a shopping cart functionality",
            "Design a database schema for an e-commerce platform",
            "Implement OAuth2 authentication flow",
            "Create an admin dashboard with charts",
            "Refactor legacy Java codebase to use modern patterns"
    };

    @PostMapping("/jobs/{count}")
    public ResponseEntity<List<Job>> createRandomJobs(@PathVariable int count) {
        List<Company> companies = companyService.getAllCompanies();
        if (companies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Random random = new Random();
        for (int i = 0; i < count; i++) {
            String title = TITLES[random.nextInt(TITLES.length)];
            String description = DESCRIPTIONS[random.nextInt(DESCRIPTIONS.length)];
            JobType jobType = JobType.values()[random.nextInt(JobType.values().length)];
            double earnings = 100 + random.nextDouble() * 900;
            Company company = companies.get(random.nextInt(companies.size()));

            Job job = new Job(title, description, jobType, earnings, company.getId());
            jobRepository.save(job);
        }

        return new ResponseEntity<>(jobRepository.findAll(), HttpStatus.OK);
    }
}
