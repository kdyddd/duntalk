package com.duntalk.domain.community.repository;

import com.duntalk.domain.community.dto.CommunityPostListDto;
import com.duntalk.domain.community.entity.CommunityPost;
import com.duntalk.domain.community.type.CommunityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommunityPostRepository extends JpaRepository<CommunityPost, Long> {

    @Query("""
            SELECT new com.duntalk.domain.community.dto.CommunityPostListDto(
                c.id,
                c.type,
                c.title,
                c.writer.id,
                d.adventureName,
                i.itemId,
                i.itemName,
                c.createdAt,
                c.viewCount,
                c.likeCount
            )
            FROM CommunityPost c
            LEFT JOIN DnfCharacter d ON d.member = c.writer
            LEFT JOIN c.item i
            WHERE c.deleted = false
              AND (:type IS NULL OR c.type = :type)
              AND (:keyword IS NULL
                  OR c.title LIKE CONCAT('%', :keyword, '%')
                  OR c.content LIKE CONCAT('%', :keyword, '%'))
            """)
    Page<CommunityPostListDto> findPostList(@Param("type") CommunityType type, @Param("keyword") String keyword, Pageable pageable);
}
