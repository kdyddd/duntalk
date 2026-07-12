package com.duntalk.domain.item.entity;

import com.duntalk.domain.item.dto.NeopleItemDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id
    private String itemId;

    private String itemName;

    private String itemRarity;

    private String itemType;

    private String itemTypeDetail;

    public static Item from(NeopleItemDto dto) {
        Item item = new Item();
        item.itemId = dto.getItemId();
        item.itemName = dto.getItemName();
        item.itemRarity = dto.getItemRarity();
        item.itemType = dto.getItemType();
        item.itemTypeDetail = dto.getItemTypeDetail();
        return item;
    }

}
