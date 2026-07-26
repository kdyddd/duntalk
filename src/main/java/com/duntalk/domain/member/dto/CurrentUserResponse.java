package com.duntalk.domain.member.dto;

import com.duntalk.domain.member.type.MemberRole;
import com.duntalk.domain.member.type.ServerId;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record CurrentUserResponse (
        Integer memberId,
        MemberRole role,
        String adventureName,
        ServerId serverId,
        String characterId,
        String characterName
){}
