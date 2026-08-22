package com.duntalk.domain.community.dto;

import com.duntalk.domain.community.type.CommunityType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommunityPostRequest (

        @NotNull(message = "게시글 유형을 선택해주세요.")
        CommunityType type,

        @NotBlank(message = "제목을 입력해주세요.")
        @Size(max = 20, message = "최대 길이를 초과했습니다.")
        String title,

        @NotBlank(message = "내용을 입력해주세요.")
        String content,

        String itemId
) {
}
