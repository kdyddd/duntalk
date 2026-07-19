package com.duntalk.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaleRankingResponse {
    private String itemId;
    private String itemName;
    private Double changeRate;
}
