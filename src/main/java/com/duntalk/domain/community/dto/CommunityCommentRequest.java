package com.duntalk.domain.community.dto;

public record CommunityCommentRequest(
        String content,
        Long parentCommentId
) {
}
