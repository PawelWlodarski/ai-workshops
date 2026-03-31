package com.wlodar.jug.ai.examples.boardgame;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("ollama")
//Currently test with evaluator fails becuse of https://github.com/spring-projects/spring-ai/issues/4902
//in our case mistral returns " YES" and evaluator expects "YES"
public class SpringAiBoardGameServiceTests {

    @Autowired
    private SpringAiBoardGameService boardGameService;

    @Autowired
    private ChatClient.Builder chatClientBuilder;

    private RelevancyEvaluator relevanceEvaluator;

    @BeforeEach
    public void setup() {
        relevanceEvaluator = new RelevancyEvaluator(chatClientBuilder);
    }


    @Test
    public void testRelevanceEvaluator() {
        //given
        var userQuestion = "Why is the sky blue?";
        var question = new Question("test game",userQuestion);
        var answer = boardGameService.askQuestion(question);

        var context = List.of(
                new Document("""
                        The sky appears blue because of Rayleigh scattering.
                        Molecules in Earth's atmosphere scatter shorter wavelengths
                        of sunlight, especially blue light, more strongly than longer wavelengths.
                        """)
        );
        var evaluationRequest = new EvaluationRequest(question.question(), context ,answer.answer());

        //when
        var response = relevanceEvaluator.evaluate(evaluationRequest);

        //then
        System.out.println("Answer: " + answer.answer());
        System.out.println("Pass: " + response.isPass());
        System.out.println("Score: " + response.getScore());

        assertThat(response.isPass()).isTrue();
        assertThat(response.getScore()).isGreaterThan(0.5f);
    }

    @Test
    void evaluateRelevancy() {
        String userText = "Why is the sky blue?";
        String answerText = "yes the sky is blue.";
        var context = List.of(
                new Document("""
                    The sky appears blue because of Rayleigh scattering.
                    Molecules in Earth's atmosphere scatter shorter wavelengths
                    of sunlight, especially blue light, more strongly than longer wavelengths.
                    """)
        );

        EvaluationRequest evaluationRequest =
                new EvaluationRequest(userText, context, answerText);

        var response = relevanceEvaluator.evaluate(evaluationRequest);

        System.out.println("Pass: " + response.isPass());
        System.out.println("Score: " + response.getScore());

        assertThat(response.isPass()).isTrue();
    }

    @Test
    void shouldReturnNonEmptyAnswer() {
        //given
        var question = new Question("test game","What is the capital of France?");

        //when
        var answer = boardGameService.askQuestion(question);

        //then
        assertThat(answer).isNotNull();
        assertThat(answer.answer()).isNotBlank();
    }
}
