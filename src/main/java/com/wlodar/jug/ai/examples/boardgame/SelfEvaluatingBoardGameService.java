package com.wlodar.jug.ai.examples.boardgame;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.resilience.annotation.Retryable;


public class SelfEvaluatingBoardGameService implements BoardGameService{

    private final ChatClient chatClient;
    private final RelevancyEvaluator evaluator;


    public SelfEvaluatingBoardGameService(ChatClient.Builder chatClientBuilder) {
        var chatOptions= ChatOptions.builder()
                .model("mistral-3:8b")
                .build();


        this.chatClient = chatClientBuilder.defaultOptions(chatOptions).build();
        this.evaluator = new RelevancyEvaluator(chatClientBuilder);
    }

    @Override
    @Retryable(includes = AnswerNotRelevantException.class, maxRetries = 5)
    public Answer askQuestion(Question question) {
        var answerText = chatClient.prompt()
                .user(question.question())
                .call()
                .content();

        evaluateRelevancy(question,answerText);

        return new Answer(answerText);
    }

    private void evaluateRelevancy(Question question,String answerText) {
        var evaluationRequest = new EvaluationRequest(question.question(), answerText);
        var response = evaluator.evaluate(evaluationRequest);
        if(!response.isPass()){
            throw new AnswerNotRelevantException(question.question(), answerText);
        }
    }
}
