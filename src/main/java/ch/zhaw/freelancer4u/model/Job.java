package ch.zhaw.freelancer4u.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
@Document("job")
public class Job {
    @Id
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String description;
    @NonNull
    private JobType jobType;
    private JobState jobState = JobState.NEW;
    @NonNull
    private Double earnings;
    @NonNull
    private String companyId;
    private String freelancerId;

    public Job(String description, JobType jobType, Double earnings, String companyId) {
        this.title = description;
        this.description = description;
        this.jobType = jobType;
        this.earnings = earnings;
        this.companyId = companyId;
    }
}
