package com.duntalk.domain.community.repository;

import com.duntalk.domain.community.dto.CommunityCommentDto;
import com.duntalk.domain.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {

    @Query("""
            SELECT new com.duntalk.domain.community.dto.CommunityCommentDto(
                c.id,
                p.id,
                c.content,
                c.writer.id,
                d.adventureName,
                c.createdAt,
                c.likeCount,
                c.deleted,
                CASE WHEN l.id IS NOT NULL THEN true ELSE false END
            )
            FROM CommunityComment c
            LEFT JOIN c.parent p
            LEFT JOIN DnfCharacter d ON c.writer = d.member
            LEFT JOIN CommunityCommentLike l ON l.comment = c AND l.member.id = :memberId
            WHERE c.post.id = :communityPostId
            ORDER BY c.createdAt ASC
            """)
    List<CommunityCommentDto> findCommentList(@Param("communityPostId") Long communityPostId, @Param("memberId") Integer memberId);

    Optional<CommunityComment> findByIdAndDeletedFalse(Long commentId);
}
