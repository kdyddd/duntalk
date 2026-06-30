package com.duntalk.domain.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NeopleItemPriceDto {

    private String itemId;

    private String regDate;

    private int count;

    private int unitPrice;

}
