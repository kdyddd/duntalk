package com.duntalk.domain.member.dto;

import java.util.List;

public record NeopleEquipmentResponse (
        String characterName,
        String adventureName,
        List<NeopleEquipmentDto> equipment
){
}
