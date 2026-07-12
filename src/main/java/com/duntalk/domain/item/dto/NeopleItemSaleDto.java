package com.duntalk.domain.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NeopleItemSaleDto {
    private String itemId;

    private String soldDate;

    private int count;

    private Long price;
}
