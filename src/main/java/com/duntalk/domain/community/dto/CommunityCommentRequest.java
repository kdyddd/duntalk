package com.duntalk.domain.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommunityCommentRequest(

        @NotBlank(message = "댓글 내용을 입력해주세요.")
        @Size(max = 255, message = "최대 길이를 초과했습니다.")
        String content,

        Long parentCommentId
) {
}
