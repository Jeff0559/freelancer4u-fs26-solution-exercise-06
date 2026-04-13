package ch.zhaw.freelancer4u.model.voucher;

import ch.zhaw.freelancer4u.model.Job;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PercentageVoucherTest {

    @Test
    void testNegativeOnePercentage() {
        Exception exception = assertThrows(RuntimeException.class, () -> new PercentageVoucher(-1));
        assertEquals("Error: Discount value must be greater zero.", exception.getMessage());
    }

    @Test
    void testZeroPercentage() {
        Exception exception = assertThrows(RuntimeException.class, () -> new PercentageVoucher(0));
        assertEquals("Error: Discount value must be greater zero.", exception.getMessage());
    }

    @Test
    void testFiftyOnePercentage() {
        Exception exception = assertThrows(RuntimeException.class, () -> new PercentageVoucher(51));
        assertEquals("Error: Discount value must less or equal 50.", exception.getMessage());
    }

    @Test
    void testTwoJobsWithMockito() {
        Job job1 = mock(Job.class);
        when(job1.getEarnings()).thenReturn(42.0);
        Job job2 = mock(Job.class);
        when(job2.getEarnings()).thenReturn(77.0);
        PercentageVoucher voucher = new PercentageVoucher(42);
        assertEquals(49.98, voucher.getDiscount(List.of(job1, job2)), 0.001);
    }
}
