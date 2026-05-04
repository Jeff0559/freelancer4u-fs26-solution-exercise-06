package ch.zhaw.freelancer4u.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobStateAggregationDTO;
import ch.zhaw.freelancer4u.model.JobStateChangeDTO;
import ch.zhaw.freelancer4u.model.Mail;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.security.Roles;
import ch.zhaw.freelancer4u.service.JobService;
import ch.zhaw.freelancer4u.service.MailService;
import ch.zhaw.freelancer4u.service.UserService;

@RestController
@RequestMapping("/api/service")
public class JobServiceController {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceController.class);

    @Autowired
    JobService jobService;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @PutMapping("/assignjob")
    public ResponseEntity<Job> assignJob(@RequestBody JobStateChangeDTO changeS) {
        if (!userService.userHasRole(Roles.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String freelancerId = changeS.getFreelancerId();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.assignJob(jobId, freelancerId);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/completejob")
    public ResponseEntity<Job> completeJob(@RequestBody JobStateChangeDTO changeS) {
        if (!userService.userHasRole(Roles.ADMIN)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        String freelancerId = changeS.getFreelancerId();
        String jobId = changeS.getJobId();
        Optional<Job> job = jobService.completeJob(jobId, freelancerId);
        if (job.isPresent()) {
            return new ResponseEntity<>(job.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/me/assignjob")
    public ResponseEntity<Job> assignToMe(@RequestParam String jobId) {
        String userEmail = userService.getEmail();
        Optional<Job> job = jobService.assignJob(jobId, userEmail);
        if (job.isPresent()) {
            Job assignedJob = job.get();
            String body = String.format(
                "Job-Beschreibung: %s%nJob-Typ: %s%nEarnings: %.2f%nStatus: %s",
                assignedJob.getDescription(), assignedJob.getJobType(),
                assignedJob.getEarnings(), assignedJob.getJobState());
            Mail mail = new Mail(userEmail, "Job zugewiesen: " + assignedJob.getDescription(), body);
            boolean sent = mailService.sendMail(mail);
            if (!sent) {
                logger.warn("Mail-Versand nach Zuweisung fehlgeschlagen für Job {}", jobId);
            }
            return new ResponseEntity<>(assignedJob, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/me/completejob")
    public ResponseEntity<Job> completeMyJob(@RequestParam String jobId) {
        String userEmail = userService.getEmail();
        Optional<Job> job = jobService.completeJob(jobId, userEmail);
        if (job.isPresent()) {
            Job completedJob = job.get();
            String body = String.format(
                "Job-Beschreibung: %s%nJob-Typ: %s%nEarnings: %.2f%nStatus: %s",
                completedJob.getDescription(), completedJob.getJobType(),
                completedJob.getEarnings(), completedJob.getJobState());
            Mail mail = new Mail(userEmail, "Job abgeschlossen: " + completedJob.getDescription(), body);
            boolean sent = mailService.sendMail(mail);
            if (!sent) {
                logger.warn("Mail-Versand nach Abschluss fehlgeschlagen für Job {}", jobId);
            }
            return new ResponseEntity<>(completedJob, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/jobdashboard")
    public List<JobStateAggregationDTO> getJobStateAggregation(@RequestParam String company) {
        return jobRepository.getJobStateAggregation(company);
    }
}
