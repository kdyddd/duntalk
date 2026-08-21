package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import com.duntalk.domain.item.dto.AuctionSummaryResponse;
import com.duntalk.domain.item.dto.SaleSummaryDto;
import com.duntalk.domain.item.entity.AuctionHourSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionHourSummaryRepository extends JpaRepository<AuctionHourSummary, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.AuctionSummaryDto(" +
            "s.item, MIN(s.minPrice), CAST(AVG(s.count) AS long)) " +
            "FROM AuctionHourSummary s " +
            "WHERE s.startTime >= :start AND s.startTime < :end " +
            "GROUP BY s.item")
    List<AuctionSummaryDto> findDayRollup(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);

    @Query("SELECT new com.duntalk.domain.item.dto.AuctionSummaryResponse(" +
            "s.startTime, s.minPrice, s.count) " +
            "FROM AuctionHourSummary s " +
            "WHERE s.item.itemId = :itemId AND s.startTime >= :start AND s.startTime < :end " +
            "ORDER BY s.startTime ASC")
    List<AuctionSummaryResponse> findAuctionHourSummaries(@Param("itemId") String itemId,
                                                             @Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end);

    @Modifying
    @Query("DELETE FROM AuctionHourSummary a WHERE a.startTime < :cutoff")
    int deleteBefore(@Param("cutoff") LocalDateTime cutoff);
}
