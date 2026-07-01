package com.duntalk.domain.item.entity;

import com.duntalk.domain.item.dto.NeopleItemAuctionDto;
import com.duntalk.domain.item.dto.NeopleItemDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class ItemAuctionHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime regDate;

    private int count;

    private int unitPrice;

    public static ItemAuctionHistory from(NeopleItemAuctionDto dto, Item item) {
        ItemAuctionHistory itemAuctionHistory = new ItemAuctionHistory();
        itemAuctionHistory.item = item;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        itemAuctionHistory.regDate = LocalDateTime.parse(dto.getRegDate(), formatter);
        itemAuctionHistory.count = dto.getCount();
        itemAuctionHistory.unitPrice = dto.getUnitPrice();
        return itemAuctionHistory;
    }

}
