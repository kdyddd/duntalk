package com.duntalk.domain.item.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NeopleItemDto {

    private String itemId;

    private String itemName;

    private String itemRarity;

    private String itemType;

    private String itemTypeDetail;

    private List<Job> jobs;

}
