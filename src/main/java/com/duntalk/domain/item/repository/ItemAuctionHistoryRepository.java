package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import com.duntalk.domain.item.entity.ItemAuctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemAuctionHistoryRepository extends JpaRepository<ItemAuctionHistory, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.AuctionSummaryDto(" +
            "h.item, MIN(h.unitPrice), SUM(h.count)) " +
            "FROM ItemAuctionHistory h " +
            "WHERE h.regDate >= :start AND h.regDate < :end " +
            "GROUP BY h.item")
    List<AuctionSummaryDto> findAuctionStats(@Param("start") LocalDateTime start,
                                  @Param("end") LocalDateTime end);

}
