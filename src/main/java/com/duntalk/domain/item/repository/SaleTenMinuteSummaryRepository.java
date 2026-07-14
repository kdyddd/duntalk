package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.SaleSummaryDto;
import com.duntalk.domain.item.dto.SaleSummaryResponse;
import com.duntalk.domain.item.entity.SaleTenMinuteSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleTenMinuteSummaryRepository extends JpaRepository<SaleTenMinuteSummary, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.SaleSummaryDto(" +
            "s.item, SUM(s.totalPrice), SUM(s.totalCount)) " +
            "FROM SaleTenMinuteSummary s " +
            "WHERE s.startTime >= :start AND s.startTime < :end " +
            "GROUP BY s.item")
    List<SaleSummaryDto> findHourRollup(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

    @Query("SELECT new com.duntalk.domain.item.dto.SaleSummaryResponse(" +
            "s.startTime, s.totalPrice, s.totalCount, CAST(s.totalPrice / s.totalCount AS integer)) " +
            "FROM SaleTenMinuteSummary s " +
            "WHERE s.item.itemId = :itemId AND s.startTime >= :start AND s.startTime < :end " +
            "ORDER BY s.startTime ASC")
    List<SaleSummaryResponse> findSaleTenMinuteSummaries(@Param("itemId") String itemId,
                                            @Param("start") LocalDateTime start,
                                             @Param("end") LocalDateTime end);
}
