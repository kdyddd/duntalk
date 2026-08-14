package com.duntalk.domain.item.dto;

import com.duntalk.domain.item.entity.Item;

public record ItemAutocompleteResponse(
        String itemId,
        String itemName,
        String itemRarity,
        String itemTypeDetail
) {
    public static ItemAutocompleteResponse from (Item item) {
        return new ItemAutocompleteResponse(
                item.getItemId(),
                item.getItemName(),
                item.getItemRarity(),
                item.getItemTypeDetail()
        );
    }
}
