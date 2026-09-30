package com.duntalk.domain.megaphone.dto;

public record ModerationResponse(
        int profanity,
        int harassment,
        int derogatory,
        int sexual,
        int hate,
        int spam,
        int personalInfo,
        int overallUnsafe,
        int promptInjection
) {
}
