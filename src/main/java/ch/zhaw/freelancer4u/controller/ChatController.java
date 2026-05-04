package ch.zhaw.freelancer4u.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.freelancer4u.tools.FreelancerTools;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private FreelancerTools freelancerTools;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String response = chatClient.prompt()
                .user(request.message())
                .tools(freelancerTools)
                .call()
                .content();
        return new ChatResponse(response);
    }

    public record ChatRequest(String message) {}
    public record ChatResponse(String reply) {}
}
