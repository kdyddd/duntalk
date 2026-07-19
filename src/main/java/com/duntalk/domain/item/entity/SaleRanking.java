package com.duntalk.domain.item.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SaleRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Double changeRate;

    @Column(nullable = false)
    private LocalDateTime startTime;

    private SaleRanking(
            Item item,
            Double changeRate,
            LocalDateTime startTime
    ) {
        this.item = item;
        this.changeRate = changeRate;
        this.startTime = startTime;
    }

    public static SaleRanking create(
            Item item,
            Double changeRate,
            LocalDateTime startTime
    ) {
        return new SaleRanking(
                item,
                changeRate,
                startTime
        );
    }
}