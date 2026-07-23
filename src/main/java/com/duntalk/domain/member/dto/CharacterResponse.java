package com.duntalk.domain.member.dto;


public record CharacterResponse(
        String characterId,
        String characterName,
        Integer level,
        String job
) {

    public static CharacterResponse from(NeopleCharacterDto dto) {
        return new CharacterResponse(
                dto.characterId(),
                dto.characterName(),
                dto.level(),
                dto.jobGrowName()
        );
    }
}
