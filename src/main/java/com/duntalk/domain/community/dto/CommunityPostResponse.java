package com.duntalk.domain.community.dto;

import com.duntalk.domain.community.type.CommunityType;

import java.time.LocalDateTime;

public record CommunityPostResponse(
        Long communityPostId,
        CommunityType type,
        String title,
        String content,
        String writerName,
        String itemId,
        String itemName,
        LocalDateTime createdAt,
        int viewCount,
        int likeCount,
        boolean isWriter,
        boolean liked
) {

    public static CommunityPostResponse from(CommunityPostDto dto, String writerName, boolean isWriter, boolean liked) {
        return new CommunityPostResponse(
                dto.communityPostId(),
                dto.type(),
                dto.title(),
                dto.content(),
                writerName,
                dto.itemId(),
                dto.itemName(),
                dto.createdAt(),
                dto.viewCount(),
                dto.likeCount(),
                isWriter,
                liked
        );
    }
}
