package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import com.duntalk.domain.item.entity.AuctionDaySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionDaySummaryRepository extends JpaRepository<AuctionDaySummary, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.AuctionSummaryDto(" +
            "s.item, MIN(s.minPrice), CAST(AVG(s.count) AS long)) " +
            "FROM AuctionDaySummary s " +
            "WHERE s.startTime >= :start AND s.startTime < :end " +
            "GROUP BY s.item")
    List<AuctionSummaryDto> findWeekRollup(@Param("start") LocalDateTime start,
                                          @Param("end") LocalDateTime end);
}

