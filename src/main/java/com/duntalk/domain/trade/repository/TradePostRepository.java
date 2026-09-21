package com.duntalk.domain.trade.repository;

import com.duntalk.domain.trade.dto.TradePostListDto;
import com.duntalk.domain.trade.entity.TradePost;
import com.duntalk.domain.trade.type.TradePostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TradePostRepository extends JpaRepository<TradePost, Long> {

    @Query("""
            SELECT new com.duntalk.domain.trade.dto.TradePostListDto(
                t.id,
                i.itemId,
                i.itemName,
                d.adventureName,
                t.price,
                t.quantity,
                t.createdAt,
                t.type,
                t.status
            )
            FROM TradePost t
            JOIN DnfCharacter d ON d.member = t.writer
            JOIN t.item i
            WHERE t.deleted = false
              AND (:type IS NULL OR t.type = :type)
              AND (:keyword IS NULL
                  OR i.itemName LIKE CONCAT('%', :keyword, '%'))
            """)
    Page<TradePostListDto> findPostList(TradePostType type, String keyword, Pageable pageable);
}
