package ch.zhaw.freelancer4u.model.voucher;

import ch.zhaw.freelancer4u.model.Job;
import java.util.List;

public class FiveBucksVoucher extends Voucher {

    @Override
    public double getDiscount(List<Job> jobs) {
        double sum = jobs.stream()
                .mapToDouble(Job::getEarnings)
                .sum();
        return sum >= 10.0 ? 5.0 : 0.0;
    }
}
