package com.duntalk.domain.item.entity;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuctionWeekSummary {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime startTime;

    private Integer minPrice;

    private Long count;

    private AuctionWeekSummary(Item item,LocalDateTime startTime, Integer minPrice, Long count) {
        this.item = item;
        this.startTime = startTime;
        this.minPrice = minPrice;
        this.count = count;
    }

    public static AuctionWeekSummary from(AuctionSummaryDto dto, LocalDateTime startTime) {
        return new AuctionWeekSummary(dto.getItem(), startTime, dto.getMinPrice(), dto.getCount());
    }


}
