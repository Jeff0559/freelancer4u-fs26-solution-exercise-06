package ch.zhaw.freelancer4u.model.voucher;

import ch.zhaw.freelancer4u.model.Job;
import java.util.List;

public abstract class Voucher {
    public abstract double getDiscount(List<Job> jobs);
}
