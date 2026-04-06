package ch.zhaw.freelancer4u.model.voucher;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class TwoForOneVoucherTest {

    @Test
    void testDifferentTypes() {
        Job job1 = new Job("desc", JobType.IMPLEMENT, 50.0, "");
        Job job2 = new Job("desc", JobType.REVIEW, 50.0, "");
        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.IMPLEMENT);
        assertEquals(0.0, voucher.getDiscount(List.of(job1, job2)));
    }

    @Test
    void testTwoSameTypeTest() {
        Job job1 = new Job("desc", JobType.TEST, 77.0, "");
        Job job2 = new Job("desc", JobType.TEST, 33.0, "");
        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.TEST);
        assertEquals(55.0, voucher.getDiscount(List.of(job1, job2)), 0.001);
    }

    @Test
    void testThreeSameTypeReview() {
        Job job1 = new Job("desc", JobType.REVIEW, 77.0, "");
        Job job2 = new Job("desc", JobType.REVIEW, 33.0, "");
        Job job3 = new Job("desc", JobType.REVIEW, 99.0, "");
        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.REVIEW);
        assertEquals(104.5, voucher.getDiscount(List.of(job1, job2, job3)), 0.001);
    }

    @Test
    void testMixedTypeThirdDifferent() {
        Job job1 = new Job("desc", JobType.REVIEW, 77.0, "");
        Job job2 = new Job("desc", JobType.REVIEW, 33.0, "");
        Job job3 = new Job("desc", JobType.TEST, 99.0, "");
        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.REVIEW);
        assertEquals(55.0, voucher.getDiscount(List.of(job1, job2, job3)), 0.001);
    }

    @ParameterizedTest
    @CsvSource({"0,0", "1,0", "2,77", "3,115.5", "4,154"})
    void testCsvSource(ArgumentsAccessor args) {
        int count = args.getInteger(0);
        double expectedDiscount = args.getDouble(1);

        List<Job> jobs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            jobs.add(new Job("desc", JobType.IMPLEMENT, 77.0, ""));
        }

        TwoForOneVoucher voucher = new TwoForOneVoucher(JobType.IMPLEMENT);
        assertEquals(expectedDiscount, voucher.getDiscount(jobs), 0.001);
    }
}
