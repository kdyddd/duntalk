package com.duntalk.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NeopleCharacterResponse(
        List<NeopleCharacterDto> rows
) {
}
