package com.duntalk.domain.community.dto;

import com.duntalk.domain.community.type.CommunityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommunityPostRequest (

        @NotNull(message = "게시글 유형을 선택해주세요.")
        CommunityType type,

        @NotBlank(message = "제목을 입력해주세요.")
        String title,

        @NotBlank(message = "내용을 입력해주세요.")
        String content,

        String itemId
) {
}
