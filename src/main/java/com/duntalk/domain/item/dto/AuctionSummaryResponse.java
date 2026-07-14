package com.duntalk.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AuctionSummaryResponse {
    private LocalDateTime startTime;
    private Integer minPrice;
    private Long count;
}
