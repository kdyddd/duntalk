package com.duntalk.domain.item.dto;

import com.duntalk.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaleRankingDto {
    private Item item;
    private Integer avgPrice;
}
