package com.duntalk.domain.member.dto;

import com.duntalk.domain.member.type.ServerId;

import java.time.LocalDateTime;

public record PendingCharacterVerification (
        Integer memberId,
        ServerId serverId,
        String characterId,
        String slotId,
        LocalDateTime expiresAt
){
    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }

    public boolean belongsTo(Integer memberId) {
        return this.memberId.equals(memberId);
    }
}
