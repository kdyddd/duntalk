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
                c.deleted
            )
            FROM CommunityComment c
            LEFT JOIN c.parent p
            LEFT JOIN DnfCharacter d ON c.writer = d.member
            WHERE c.post.id = :communityPostId
            ORDER BY c.createdAt ASC
            """)
    List<CommunityCommentDto> findCommentList(@Param("communityPostId") Long communityPostId);

    Optional<CommunityComment> findByIdAndDeletedFalse(Long commentId);

    int countByPostIdAndDeletedFalse(Long communityPostId);
}
