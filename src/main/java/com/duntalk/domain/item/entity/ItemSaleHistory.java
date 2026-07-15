package com.duntalk.domain.item.entity;

import com.duntalk.domain.item.dto.NeopleItemSaleDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
public class ItemSaleHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime soldDate;

    private int count;

    private Long price;

    public static ItemSaleHistory from(NeopleItemSaleDto dto, Item item) {
        ItemSaleHistory itemSaleHistory = new ItemSaleHistory();
        itemSaleHistory.item = item;
        itemSaleHistory.soldDate = dto.getSoldDate();
        itemSaleHistory.count = dto.getCount();
        itemSaleHistory.price = dto.getPrice();
        return itemSaleHistory;
    }

}
