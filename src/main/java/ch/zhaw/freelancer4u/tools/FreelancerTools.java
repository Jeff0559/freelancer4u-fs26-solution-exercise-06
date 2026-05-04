package ch.zhaw.freelancer4u.tools;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.CompanyService;

@Component
public class FreelancerTools {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private CompanyService companyService;

    @Tool(description = "Gibt alle verfügbaren Jobs zurück")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Tool(description = "Gibt alle Unternehmen zurück")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @Tool(description = "Erstellt ein neues Unternehmen mit Name und E-Mail-Adresse")
    public String createCompany(String name, String email) {
        companyService.createCompany(name, email);
        return "Unternehmen '" + name + "' wurde erfolgreich erstellt.";
    }

    @Tool(description = "Erstellt einen neuen Job. Falls das Unternehmen noch nicht existiert, "
            + "muss zuerst createCompany aufgerufen werden. Der jobType muss einer der Werte "
            + "IMPLEMENT, TEST, REVIEW oder OTHER sein.")
    public String createJob(String title, String description, String jobType,
                            double earnings, String companyName) {
        List<Company> companies = companyService.getAllCompanies();
        Company company = companies.stream()
                .filter(c -> c.getName().equalsIgnoreCase(companyName))
                .findFirst()
                .orElse(null);

        if (company == null) {
            return "Fehler: Unternehmen '" + companyName + "' existiert nicht. "
                    + "Bitte erstelle es zuerst mit createCompany.";
        }

        try {
            JobType type = JobType.valueOf(jobType.toUpperCase());
            Job job = new Job(title, description, type, earnings, company.getId());
            jobRepository.save(job);
            return "Job '" + title + "' wurde erfolgreich für Unternehmen '"
                    + companyName + "' erstellt.";
        } catch (IllegalArgumentException e) {
            return "Fehler: Ungültiger jobType. Erlaubt sind: IMPLEMENT, TEST, REVIEW, OTHER.";
        }
    }
}
