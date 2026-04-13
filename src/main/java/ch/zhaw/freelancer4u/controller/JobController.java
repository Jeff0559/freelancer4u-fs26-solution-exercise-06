package ch.zhaw.freelancer4u.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobCreateDTO;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.CompanyService;
import ch.zhaw.freelancer4u.service.UserService;

@RestController
@RequestMapping("/api")
public class JobController {

    @Autowired
    JobRepository jobRepository;

    @Autowired
    CompanyService companyService;

    @Autowired
    UserService userService;

    @PostMapping("/job")
    public ResponseEntity<Job> createJob(@RequestBody JobCreateDTO cDTO) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!companyService.companyExists(cDTO.getCompanyId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Job job = new Job(cDTO.getTitle(), cDTO.getDescription(), cDTO.getJobType(), cDTO.getEarnings(), cDTO.getCompanyId());
        Job j = jobRepository.save(job);
        return new ResponseEntity<>(j, HttpStatus.CREATED);
    }

    @GetMapping("/job")
    public ResponseEntity<List<Job>> getAllJobs(@RequestParam(required = false) Double min, 
            @RequestParam(required = false) JobType type) {
        List<Job> allJobs;
        if (min == null && type == null) {
            allJobs = jobRepository.findAll();
        } else {
            if (min != null && type != null) {
                allJobs = jobRepository.findByJobTypeAndEarningsGreaterThan(type, min);
            } else if (type != null) {
                allJobs = jobRepository.findByJobType(type);
            } else {
                allJobs = jobRepository.findByEarningsGreaterThan(min);
            }
        }
        return new ResponseEntity<>(allJobs, HttpStatus.OK);
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable String id) {
        var job = jobRepository.findById(id);
        if (job.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(job.get(), HttpStatus.OK);
    }

    @DeleteMapping("/job/{id}")
    public ResponseEntity<String> deleteJobById(@PathVariable String id) {
        if (!userService.userHasRole("admin")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        jobRepository.deleteById(id);
        return new ResponseEntity<>("DELETED", HttpStatus.OK);
    }
}
