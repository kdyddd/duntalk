package com.duntalk.domain.item.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

}
