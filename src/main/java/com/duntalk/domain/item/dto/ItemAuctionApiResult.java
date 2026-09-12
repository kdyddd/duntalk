package com.duntalk.domain.item.dto;

import com.duntalk.domain.item.entity.Item;

import java.util.List;

public record ItemAuctionApiResult(
        Item item,
        List<NeopleItemAuctionDto> auctions
) {
}
