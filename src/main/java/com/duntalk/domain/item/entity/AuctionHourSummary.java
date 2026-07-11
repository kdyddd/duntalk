package com.duntalk.domain.item.entity;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AuctionHourSummary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime startTime;

    private Integer minPrice;

    private Long count;

    public static AuctionHourSummary from(AuctionSummaryDto dto, LocalDateTime startTime){
        AuctionHourSummary auctionHourSummary = new AuctionHourSummary();
        auctionHourSummary.item = dto.getItem();
        auctionHourSummary.startTime = startTime;
        auctionHourSummary.minPrice = dto.getMinPrice();
        auctionHourSummary.count = dto.getTotalCount();

        return auctionHourSummary;
    }

}
