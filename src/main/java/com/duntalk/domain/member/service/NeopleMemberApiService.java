package com.duntalk.domain.member.service;


import com.duntalk.domain.member.dto.NeopleCharacterDto;
import com.duntalk.domain.member.dto.NeopleCharacterResponse;
import com.duntalk.domain.member.dto.NeopleEquipmentResponse;
import com.duntalk.domain.member.type.ServerId;
import com.duntalk.global.exception.ExternalApiException;
import com.duntalk.global.exception.ResourceNotFoundException;
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
                .blockOptional()
                .orElseThrow(() ->
                        new ExternalApiException("네오플 API 응답이 비어 있습니다."));

        if(response.rows() == null) {
            throw new ExternalApiException("네오플 API 응답 내부 rows가 비어 있습니다.");
        }

        if(response.rows().isEmpty()) {
            throw new ResourceNotFoundException("캐릭터를 찾을 수 없습니다.");
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
                .blockOptional()
                .orElseThrow(() ->
                        new ExternalApiException("네오플 API 응답이 비어 있습니다."));

        if(response.equipment() == null) {
            throw new ExternalApiException("캐릭터 장비 정보를 불러오지 못했습니다.");
        }

        return response;
    }

}
