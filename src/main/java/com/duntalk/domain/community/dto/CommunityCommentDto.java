package com.duntalk.domain.community.dto;

import java.time.LocalDateTime;

public record CommunityCommentDto(
        Long communityCommentId,
        Long communityCommentParentId,
        String content,
        Integer writerId,
        String adventureName,
        LocalDateTime createdAt,
        int likeCount,
        boolean deleted
) {
}
