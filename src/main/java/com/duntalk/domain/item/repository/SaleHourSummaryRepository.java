package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.SaleSummaryDto;
import com.duntalk.domain.item.entity.SaleHourSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SaleHourSummaryRepository extends JpaRepository<SaleHourSummary, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.SaleSummaryDto(" +
            "s.item, SUM(s.totalPrice), SUM(s.totalCount)) " +
            "FROM SaleHourSummary s " +
            "WHERE s.startTime >= :start AND s.startTime < :end " +
            "GROUP BY s.item")
    List<SaleSummaryDto> findDayRollup(@Param("start") LocalDateTime start,
                                       @Param("end") LocalDateTime end);
}
