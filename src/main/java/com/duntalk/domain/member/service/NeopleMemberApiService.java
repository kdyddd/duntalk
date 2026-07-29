package com.duntalk.domain.member.service;

import com.duntalk.domain.item.dto.NeopleItemExplainResponse;
import com.duntalk.domain.item.dto.NeopleItemResponse;
import com.duntalk.domain.member.dto.NeopleCharacterDto;
import com.duntalk.domain.member.dto.NeopleCharacterResponse;
import com.duntalk.domain.member.dto.NeopleEquipmentDto;
import com.duntalk.domain.member.dto.NeopleEquipmentResponse;
import com.duntalk.domain.member.type.ServerId;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "캐릭터를 찾을 수 없습니다."
            );
        }

        return response.rows().get(0);
    }

    public NeopleEquipmentResponse getCharacterEquipmentAll(ServerId serverId, String characterId) {
        NeopleEquipmentResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/df/servers/{serverId}/characters/{characterId}/equip/equipment")
                        .queryParam("apikey", apiKey)
                        .build(serverId.getApiValue(), characterId))
                .retrieve()
                .bodyToMono(NeopleEquipmentResponse.class)
                .block();

        if (response == null || response.equipment() == null) {
            throw new IllegalStateException("캐릭터 장비 정보를 불러오지 못했습니다.");
        }

        return response;
    }

}
