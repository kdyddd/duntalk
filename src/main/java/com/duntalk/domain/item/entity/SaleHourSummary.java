package com.duntalk.domain.item.entity;

import com.duntalk.domain.item.dto.SaleSummaryDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleHourSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private LocalDateTime startTime;

    private Long totalPrice;

    private Long totalCount;

    private SaleHourSummary(Item item, LocalDateTime startTime, Long totalPrice, Long totalCount) {
        this.item = item;
        this.startTime = startTime;
        this.totalPrice = totalPrice;
        this.totalCount = totalCount;
    }

    public static SaleHourSummary from(SaleSummaryDto dto, LocalDateTime startTime){
        return new SaleHourSummary(dto.getItem(), startTime, dto.getTotalPrice(), dto.getTotalCount());
    }
}
