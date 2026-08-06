package com.duntalk.domain.community.entity;

import com.duntalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommunityComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private CommunityComment parent;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CommunityPost post;

    @JoinColumn(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    @Column(nullable = false)
    private String content;

    private int likeCount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deleted;

    private LocalDateTime deletedAt;

}
