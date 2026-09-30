package com.duntalk.domain.megaphone.type;

public record ModerationResult(
        boolean approved,
        String reason
) {
}
