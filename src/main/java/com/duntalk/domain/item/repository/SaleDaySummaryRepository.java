package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.SaleRankingDto;
import com.duntalk.domain.item.dto.SaleSummaryDto;
import com.duntalk.domain.item.dto.SaleSummaryResponse;
import com.duntalk.domain.item.entity.SaleDaySummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleDaySummaryRepository extends JpaRepository<SaleDaySummary, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.SaleSummaryDto(" +
            "s.item, SUM(s.totalPrice), SUM(s.totalCount), MIN(s.minPrice), MAX(s.maxPrice)) " +
            "FROM SaleDaySummary s " +
            "WHERE s.startTime >= :start AND s.startTime < :end " +
            "GROUP BY s.item")
    List<SaleSummaryDto> findWeekRollup(@Param("start") LocalDateTime start,
                                        @Param("end") LocalDateTime end);
    @Query("SELECT new com.duntalk.domain.item.dto.SaleSummaryResponse(" +
            "s.startTime, s.totalPrice, s.totalCount, CAST(s.totalPrice / s.totalCount AS integer), s.minPrice, s.maxPrice) " +
            "FROM SaleDaySummary s " +
            "WHERE s.item.itemId = :itemId AND s.startTime >= :start AND s.startTime < :end " +
            "ORDER BY s.startTime ASC")
    List<SaleSummaryResponse> findSaleDaySummaries(@Param("itemId") String itemId,
                                                    @Param("start") LocalDateTime start,
                                                    @Param("end") LocalDateTime end);

    @Query("SELECT new com.duntalk.domain.item.dto.SaleRankingDto(" +
            "s.item, CAST(s.totalPrice / s.totalCount AS integer)) " +
            "FROM SaleDaySummary s " +
            "WHERE s.startTime = :start AND s.totalCount > 0")
    List<SaleRankingDto> findAvgPrices(@Param("start") LocalDateTime start);
}
