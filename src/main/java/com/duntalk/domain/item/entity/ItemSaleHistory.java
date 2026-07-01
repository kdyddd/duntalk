package com.duntalk.domain.item.entity;

import com.duntalk.domain.item.dto.NeopleItemSaleDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class ItemSaleHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime soldDate;

    private int count;

    private int price;

    public static ItemSaleHistory from(NeopleItemSaleDto dto, Item item) {
        ItemSaleHistory itemSaleHistory = new ItemSaleHistory();
        itemSaleHistory.item = item;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        itemSaleHistory.soldDate = LocalDateTime.parse(dto.getSoldDate(), formatter);
        itemSaleHistory.count = dto.getCount();
        itemSaleHistory.price = dto.getUnitPrice();
        return itemSaleHistory;
    }

}
