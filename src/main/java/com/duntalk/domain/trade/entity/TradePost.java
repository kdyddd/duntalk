package com.duntalk.domain.trade.entity;

import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.trade.type.TradePostStatus;
import com.duntalk.domain.trade.type.TradePostType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradePost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "item_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private long price;

    @JoinColumn(name = "writer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradePostType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TradePostStatus status;

    private LocalDateTime createdAt;

    private boolean deleted;

    private LocalDateTime deletedAt;

    private TradePost(Item item, int quantity, long price, Member writer, String content, TradePostType type) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.writer = writer;
        this.content = content;
        this.type = type;
        this.status = TradePostStatus.TRADING;
        this.createdAt = LocalDateTime.now();
    }

    public static TradePost create(Item item, int quantity, long price, Member writer, String content, TradePostType type) {
        return new TradePost(item, quantity, price, writer, content, type);
    }

    public void complete() {
        this.status = TradePostStatus.COMPLETED;
    }

    public void delete() {
        if(!deleted) {
            this.deletedAt = LocalDateTime.now();
        }
        this.deleted = true;
    }
}
