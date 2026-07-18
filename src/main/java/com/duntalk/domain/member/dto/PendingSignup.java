package com.duntalk.domain.member.dto;

import com.duntalk.domain.member.type.SocialProvider;

public record PendingSignup(
        SocialProvider provider,
        String providerId,
        String email
) {
    public static final String SESSION_KEY = "PENDING_SIGNUP";
}
