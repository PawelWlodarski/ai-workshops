package com.wlodar.jug.ai.examples.boardgame;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.io.IOException;
import java.nio.charset.Charset;

@EnableWireMock(
        @ConfigureWireMock(baseUrlProperties = "openai.base.url"))
@SpringBootTest(
        properties = "spring.ai.openai.base-url=${openai.base.url}")
class SpringAiBoardGameServiceWireMockTests {

    @Value("classpath:/test-openai-response.json")
    Resource responseResource;

    @Autowired
    ChatClient.Builder chatClientBuilder;

    @BeforeEach
    public void setup() throws IOException {
        var cannedResponse =
                responseResource.getContentAsString(Charset.defaultCharset());

        WireMock.stubFor(WireMock.post("/v1/chat/completions")
                .willReturn(
                        WireMock.aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(cannedResponse)
                ));
    }

    @Test
    public void testAskQuestion() {
        var boardGameService =
                new SpringAiBoardGameService(chatClientBuilder);
        var answer =
                boardGameService.askQuestion(
                        new Question("What is the capital of France?"));
        Assertions.assertThat(answer).isNotNull();
        Assertions.assertThat(answer.answer()).isEqualTo("Paris");
    }
}