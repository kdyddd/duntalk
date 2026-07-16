package com.duntalk.domain.item.entity;

import com.duntalk.domain.item.dto.SaleSummaryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleDaySummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime startTime;

    private Long totalPrice;

    private Long totalCount;

    private Integer minPrice;

    private Integer maxPrice;

    private SaleDaySummary(Item item, LocalDateTime startTime, Long totalPrice, Long totalCount, Integer minPrice, Integer maxPrice) {
        this.item = item;
        this.startTime = startTime;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public static SaleDaySummary from(SaleSummaryDto dto, LocalDateTime startTime){
        return new SaleDaySummary(dto.getItem(), startTime, dto.getTotalPrice(), dto.getTotalCount(), dto.getMinPrice(), dto.getMaxPrice());
    }

}
