package com.duntalk.domain.community.dto;

import java.time.LocalDateTime;

public record CommunityCommentResponse(
    Long communityCommentId,
    Long communityCommentParentId,
    String content,
    String writerName,
    LocalDateTime createdAt,
    int likeCount,
    boolean isWriter,
    boolean deleted,
    boolean liked
) {
    public static CommunityCommentResponse from(CommunityCommentDto dto, String writerName, boolean isWriter, boolean liked) {
        return new CommunityCommentResponse(
                dto.communityCommentId(),
                dto.communityCommentParentId(),
                dto.content(),
                writerName,
                dto.createdAt(),
                dto.likeCount(),
                isWriter,
                dto.deleted(),
                liked
        );
    }

    public static CommunityCommentResponse deleted(CommunityCommentDto dto) {
        return new CommunityCommentResponse(
                dto.communityCommentId(),
                dto.communityCommentParentId(),
                null,
                null,
                null,
                0,
                false,
                true,
                false
        );
    }
}
