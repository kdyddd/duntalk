package com.duntalk.domain.community.entity;

import com.duntalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_community_post_like_post_member",
                        columnNames = {"post_id", "member_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityPostLike {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private CommunityPost post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private CommunityPostLike(CommunityPost post, Member member) {
        this.post = post;
        this.member = member;
    }

    public static CommunityPostLike create(CommunityPost post, Member member) {
        return new CommunityPostLike(post, member);
    }
}
