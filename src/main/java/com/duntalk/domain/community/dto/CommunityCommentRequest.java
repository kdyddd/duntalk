package com.duntalk.domain.community.dto;

import jakarta.validation.constraints.NotBlank;

public record CommunityCommentRequest(

        @NotBlank(message = "댓글 내용을 입력해주세요.")
        String content,

        Long parentCommentId
) {
}
