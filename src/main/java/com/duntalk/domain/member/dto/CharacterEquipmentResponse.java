package com.duntalk.domain.member.dto;

public record CharacterEquipmentResponse (
        String slotId,
        String slotName,
        String itemId,
        String itemName
){
    public static CharacterEquipmentResponse from(NeopleEquipmentDto dto) {
        return new CharacterEquipmentResponse(
                dto.slotId(),
                dto.slotName(),
                dto.itemId(),
                dto.itemName()
        );
    }
}
