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
                        name = "uk_community_comment_like_comment_member",
                        columnNames = {"comment_id", "member_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommunityCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private CommunityComment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private CommunityCommentLike(CommunityComment comment, Member member) {
        this.comment = comment;
        this.member = member;
    }

    public static CommunityCommentLike create(CommunityComment comment, Member member) {
        return new CommunityCommentLike(comment, member);
    }
}
