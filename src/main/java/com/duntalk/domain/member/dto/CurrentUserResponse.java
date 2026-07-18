package com.duntalk.domain.member.dto;

import com.duntalk.domain.member.type.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

public record CurrentUserResponse (
        Integer memberId,
        MemberRole role
){}
