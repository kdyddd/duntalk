package com.duntalk.domain.item.dto;

import com.duntalk.domain.item.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AuctionSummaryDto {
    private Item item;
    private Integer minPrice;
    private Long totalCount;
}
