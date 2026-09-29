package com.duntalk.domain.trade.repository;

import com.duntalk.domain.trade.dto.TradePostDto;
import com.duntalk.domain.trade.dto.TradePostListDto;
import com.duntalk.domain.trade.entity.TradePost;
import com.duntalk.domain.trade.type.TradePostStatus;
import com.duntalk.domain.trade.type.TradePostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

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
              AND (:status IS NULL OR t.status = :status)
              AND (:keyword IS NULL
                  OR i.itemName LIKE CONCAT('%', :keyword, '%'))
            """)
    Page<TradePostListDto> findPostList(@Param("type") TradePostType type, @Param("status") TradePostStatus status, @Param("keyword") String keyword, Pageable pageable);

    @Query("""
            SELECT new com.duntalk.domain.trade.dto.TradePostDto(
                t.id,
                i.itemId,
                i.itemName,
                d.adventureName,
                t.price,
                t.quantity,
                t.createdAt,
                t.type,
                t.status,
                t.content
            )
            FROM TradePost t
            JOIN DnfCharacter d ON d.member = t.writer
            JOIN t.item i
            WHERE t.deleted = false
              AND t.id = :tradePostId
            """)
    Optional<TradePostDto> getTradePost(@Param("tradePostId") Long tradePostId);

    Optional<TradePost> findByIdAndDeletedFalse(Long tradePostId);
}
