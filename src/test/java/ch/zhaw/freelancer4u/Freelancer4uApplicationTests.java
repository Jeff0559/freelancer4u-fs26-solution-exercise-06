package ch.zhaw.freelancer4u;

import ch.zhaw.freelancer4u.security.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestSecurityConfig.class)
class Freelancer4uApplicationTests {

	@Test
	void contextLoads() {
	}

}
