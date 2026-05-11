package ch.zhaw.freelancer4u;

import ch.zhaw.freelancer4u.security.TestSecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@Import(TestSecurityConfig.class)
class Freelancer4uApplicationTests {

	@MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
	OpenAiChatModel chatModel;

	@MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
	ChatClient chatClient;

	@Test
	void contextLoads() {
		// Prüft, ob der Spring Application Context fehlerfrei startet
	}

}
