package com.duntalk.domain.trade.dto;

import com.duntalk.domain.trade.type.TradePostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record TradePostRequest(

        @NotBlank
        String itemId,

        @Positive
        int quantity,

        @Positive
        long price,

        @Size(max = 40)
        String content,

        @NotNull
        TradePostType type
) {
}
