package com.duntalk.domain.item.dto;

import com.duntalk.domain.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemResponse {

    private String itemId;

    private String itemName;

    private String itemRarity;

    private String itemType;

    private String itemTypeDetail;

    private String itemExplain;

    public static ItemResponse from (Item item) {
        ItemResponse itemResponse = new ItemResponse();
        itemResponse.itemId = item.getItemId();
        itemResponse.itemName = item.getItemName();
        itemResponse.itemRarity = item.getItemRarity();
        itemResponse.itemType = item.getItemType();
        itemResponse.itemTypeDetail = item.getItemTypeDetail();
        itemResponse.itemExplain = item.getItemExplain();

        return itemResponse;
    }
}
