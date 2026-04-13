package ch.zhaw.freelancer4u.model.voucher;

import ch.zhaw.freelancer4u.model.Job;
import java.util.List;

public class PercentageVoucher extends Voucher {

    private final double percentage;

    public PercentageVoucher(double percentage) {
        if (percentage > 50) {
            throw new RuntimeException("Error: Discount value must less or equal 50.");
        }
        if (percentage <= 0) {
            throw new RuntimeException("Error: Discount value must be greater zero.");
        }
        this.percentage = percentage;
    }

    @Override
    public double getDiscount(List<Job> jobs) {
        double sum = jobs.stream()
                .mapToDouble(Job::getEarnings)
                .sum();
        return sum * (percentage / 100.0);
    }
}
