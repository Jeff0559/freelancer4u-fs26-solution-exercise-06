package ch.zhaw.freelancer4u.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAiConfig {

    @Bean
    public ChatClient chatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem("Du bist ein hilfreicher Assistent für die Freelancer4U Plattform. "
                        + "Du hilfst Nutzern dabei, passende Jobs zu finden und neue Jobs oder "
                        + "Unternehmen zu erstellen. Antworte immer auf Deutsch.")
                .build();
    }
}
