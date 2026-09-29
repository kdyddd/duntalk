package com.duntalk.domain.trade.dto;

import com.duntalk.domain.trade.type.TradePostStatus;
import com.duntalk.domain.trade.type.TradePostType;

import java.time.LocalDateTime;

public record TradePostListResponse(
        Long tradePostId,
        String itemId,
        String itemName,
        int quantity,
        long price,
        String writerName,
        TradePostType type,
        TradePostStatus status,
        LocalDateTime createdAt
) {
    public static TradePostListResponse from(TradePostListDto dto) {
        return new TradePostListResponse(
                dto.tradePostId(),
                dto.itemId(),
                dto.itemName(),
                dto.quantity(),
                dto.price(),
                dto.adventureName(),
                dto.type(),
                dto.status(),
                dto.createdAt()
        );
    }
}
