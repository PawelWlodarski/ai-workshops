package com.wlodar.jug.ai.examples.boardgame;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SpringAiBoardGameService implements BoardGameService {

    private final ChatClient chatClient;

    public SpringAiBoardGameService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public Answer askQuestion(Question question) {
        ChatClient.CallResponseSpec call = chatClient.prompt()
                .user(question.question())
                .call();
        var answer = call.content();
        return new Answer(answer);
    }
}
