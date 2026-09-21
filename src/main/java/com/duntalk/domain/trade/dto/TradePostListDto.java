package com.duntalk.domain.trade.dto;

import com.duntalk.domain.trade.type.TradePostStatus;
import com.duntalk.domain.trade.type.TradePostType;

import java.time.LocalDateTime;

public record TradePostListDto(
        Long tradePostId,
        String itemId,
        String itemName,
        String adventureName,
        long price,
        int quantity,
        LocalDateTime createdAt,
        TradePostType type,
        TradePostStatus status
) {
}
