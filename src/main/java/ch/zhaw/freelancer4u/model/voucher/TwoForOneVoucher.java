package ch.zhaw.freelancer4u.model.voucher;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import java.util.List;
import java.util.stream.Collectors;

public class TwoForOneVoucher extends Voucher {

    private final JobType jobType;

    public TwoForOneVoucher(JobType jobType) {
        this.jobType = jobType;
    }

    @Override
    public double getDiscount(List<Job> jobs) {
        List<Job> matching = jobs.stream()
                .filter(j -> j.getJobType() == jobType)
                .collect(Collectors.toList());
        if (matching.size() < 2) return 0.0;
        double sum = matching.stream()
                .mapToDouble(Job::getEarnings)
                .sum();
        return sum / 2.0;
    }
}
