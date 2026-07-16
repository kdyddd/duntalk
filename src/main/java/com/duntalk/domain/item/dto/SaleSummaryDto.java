package com.duntalk.domain.item.dto;

import com.duntalk.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SaleSummaryDto {
    private Item item;
    private Long totalPrice;
    private Long totalCount;
    private Integer minPrice;
    private Integer maxPrice;
}
