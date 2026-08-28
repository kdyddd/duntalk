package com.duntalk.domain.community.entity;

import com.duntalk.domain.community.type.CommunityType;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CommunityType type;

    @JoinColumn(name = "item_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private int viewCount;

    private int likeCount;

    private int commentCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deleted;

    private LocalDateTime deletedAt;

    private CommunityPost(Member writer, CommunityType type, Item item, String title, String content) {
        this.writer = writer;
        this.type = type;
        this.item = item;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public static CommunityPost create(Member writer, CommunityType type, Item item, String title, String content) {
        return new CommunityPost(writer, type, item, title, content);
    }

    public void update(CommunityType type, Item item, String title, String content) {
        this.type = type;
        this.item = item;
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        if(!deleted) {
            this.deletedAt = LocalDateTime.now();
        }
        this.deleted = true;
    }

    public void increaseLiked() {
        this.likeCount ++;
    }

    public void decreaseLiked() {
        this.likeCount --;
    }

}
