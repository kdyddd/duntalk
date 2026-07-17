package com.duntalk.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CurrentUserResponse {
    private String providerId;
    private String email;
    private String name;
}
