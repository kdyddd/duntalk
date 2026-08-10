package com.duntalk.domain.community.dto;

import com.duntalk.domain.community.type.CommunityType;
import com.duntalk.domain.member.type.ServerId;

import java.time.LocalDateTime;

public record CommunityPostDto(
        Long communityPostId,
        CommunityType type,
        String title,
        String content,
        Integer writerId,
        String adventureName,
        String itemId,
        String itemName,
        LocalDateTime createdAt,
        int viewCount,
        int likeCount
) {
}
