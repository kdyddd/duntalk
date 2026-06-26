package com.duntalk.domain.item.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Item {

    @Id
    private String itemId;

    private String itemName;

    private String itemRarity;

    private String itemType;

    private String itemTypeDetail;

}
