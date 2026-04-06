package ch.zhaw.freelancer4u.model.voucher;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PercentageVoucherTest {

    @Test
    void testEmpty() {
        PercentageVoucher voucher = new PercentageVoucher(10);
        assertEquals(0.0, voucher.getDiscount(List.of()));
    }

    @ParameterizedTest
    @ValueSource(doubles = {1, 2, 5, 20, 49, 50})
    void testSingleJobFifty(double percent) {
        Job job = new Job("desc", JobType.IMPLEMENT, 50.0, "");
        PercentageVoucher voucher = new PercentageVoucher(percent);
        double expected = 50.0 * (percent / 100.0);
        assertEquals(expected, voucher.getDiscount(List.of(job)), 0.001);
    }

    @Test
    void testTwoJobsFortyTwoPercent() {
        Job job1 = new Job("desc1", JobType.IMPLEMENT, 42.0, "");
        Job job2 = new Job("desc2", JobType.REVIEW, 77.0, "");
        PercentageVoucher voucher = new PercentageVoucher(42);
        assertEquals(49.98, voucher.getDiscount(List.of(job1, job2)), 0.001);
    }
}
