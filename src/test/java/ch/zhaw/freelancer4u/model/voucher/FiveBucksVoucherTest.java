package ch.zhaw.freelancer4u.model.voucher;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FiveBucksVoucherTest {

    @Test
    void testEmpty() {
        FiveBucksVoucher voucher = new FiveBucksVoucher();
        assertEquals(0.0, voucher.getDiscount(List.of()));
    }

    @Test
    void testTen() {
        Job job = new Job("desc", JobType.IMPLEMENT, 10.0, "");
        FiveBucksVoucher voucher = new FiveBucksVoucher();
        assertEquals(5.0, voucher.getDiscount(List.of(job)));
    }

    @Test
    void testBelowTen() {
        Job job = new Job("desc", JobType.IMPLEMENT, 9.99, "");
        FiveBucksVoucher voucher = new FiveBucksVoucher();
        assertEquals(0.0, voucher.getDiscount(List.of(job)));
    }
}
