package com.duntalk.domain.community.dto;

import com.duntalk.domain.community.type.CommunityType;

import java.time.LocalDateTime;

public record CommunityPostListResponse(
        Long communityPostId,
        CommunityType type,
        String title,
        String writerName,
        String itemId,
        String itemName,
        LocalDateTime createdAt,
        int viewCount,
        int likeCount,
        int commentCount
) {
    public static CommunityPostListResponse from(CommunityPostListDto dto, String writerName, int commentCount) {
        return new CommunityPostListResponse(
                dto.communityPostId(),
                dto.type(),
                dto.title(),
                writerName,
                dto.itemId(),
                dto.itemName(),
                dto.createdAt(),
                dto.viewCount(),
                dto.likeCount(),
                commentCount
        );
    }
}
