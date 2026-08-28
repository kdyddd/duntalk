package com.duntalk.domain.community.dto;

import com.duntalk.domain.community.type.CommunityType;

import java.time.LocalDateTime;

public record CommunityPostListDto(
        Long communityPostId,
        CommunityType type,
        String title,
        Integer writerId,
        String adventureName,
        String itemId,
        String itemName,
        LocalDateTime createdAt,
        int viewCount,
        int likeCount,
        int commentCount
) {
}
