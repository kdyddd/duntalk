package com.duntalk.domain.megaphone.service;

import com.duntalk.domain.megaphone.type.ModerationResult;
import com.openai.client.OpenAIClient;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.StructuredResponseCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class ModerationService {
    private final OpenAIClient openAIClient;

    public ModerationResult moderate(String content) {
        String prompt = loadPrompt();

        StructuredResponseCreateParams<ModerationResult> params = ResponseCreateParams.builder()
                .model("gpt-5.6-luna")
                .instructions(prompt)
                .input(content)
                .text(ModerationResult.class)
                .maxOutputTokens(300)
                .build();

        return openAIClient.responses()
                .create(params)
                .output()
                .stream()
                .flatMap(item -> item.message().stream())
                .flatMap(message -> message.content().stream())
                .flatMap(contentPart -> contentPart.outputText().stream())
                .findFirst()
                .orElseThrow(() ->
                        new IllegalStateException("AI 검수 결과가 없습니다."));
    }

    private String loadPrompt() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("prompts/megaphone-moderation.txt");

            return resource.getContentAsString(StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("메가폰 검수 프롬프트를 불러오지 못했습니다.", e);
        }
    }
}
