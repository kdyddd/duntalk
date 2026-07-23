package com.duntalk.domain.member.service;

import com.duntalk.domain.item.dto.NeopleItemExplainResponse;
import com.duntalk.domain.item.dto.NeopleItemResponse;
import com.duntalk.domain.member.dto.NeopleCharacterDto;
import com.duntalk.domain.member.dto.NeopleCharacterResponse;
import com.duntalk.domain.member.type.ServerId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class NeopleMemberApiService {

    private final WebClient webClient;

    @Value("${neople.api-key}")
    private String apiKey;

    public NeopleCharacterDto searchCharacter(ServerId serverId, String characterName) {
        NeopleCharacterResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/df/servers/{serverId}/characters")
                        .queryParam("characterName", characterName)
                        .queryParam("limit", 1)
                        .queryParam("apikey", apiKey)
                        .build(serverId.getApiValue()))
                .retrieve()
                .bodyToMono(NeopleCharacterResponse.class)
                .block();

        if (response == null || response.rows() == null || response.rows().isEmpty()) {
            throw new IllegalArgumentException("캐릭터를 찾을 수 없습니다.");
        }

        return response.rows().get(0);
    }

}
