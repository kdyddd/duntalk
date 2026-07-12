package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import com.duntalk.domain.item.entity.AuctionTenMinuteSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionTenMinuteSummaryRepository extends JpaRepository<AuctionTenMinuteSummary, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.AuctionSummaryDto(" +
            "s.item, MIN(s.minPrice), CAST(AVG(s.count) AS long)) " +
            "FROM AuctionTenMinuteSummary s " +
            "WHERE s.startTime >= :start AND s.startTime < :end " +
            "GROUP BY s.item")
    List<AuctionSummaryDto> findHourRollup(@Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);
}
