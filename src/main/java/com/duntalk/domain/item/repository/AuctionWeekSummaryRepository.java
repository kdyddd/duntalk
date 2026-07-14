package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.AuctionSummaryResponse;
import com.duntalk.domain.item.entity.AuctionWeekSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionWeekSummaryRepository extends JpaRepository<AuctionWeekSummary, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.AuctionSummaryResponse(" +
            "s.startTime, s.minPrice, s.count) " +
            "FROM AuctionWeekSummary s " +
            "WHERE s.item.itemId = :itemId AND s.startTime >= :start AND s.startTime < :end " +
            "ORDER BY s.startTime ASC")
    List<AuctionSummaryResponse> findAuctionWeekSummaries(@Param("itemId") String itemId,
                                                         @Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end);
}
