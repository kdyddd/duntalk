package com.duntalk.domain.community.dto;

import com.duntalk.domain.community.type.CommunityType;

public record CommunityPostRequest (
        CommunityType type,
        String title,
        String content,
        String itemId
) {
}
