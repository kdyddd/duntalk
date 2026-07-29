package com.duntalk.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NeopleCharacterDto(
        String serverId,
        String characterId,
        String characterName,
        Integer level,
        String jobGrowName
) {
}
