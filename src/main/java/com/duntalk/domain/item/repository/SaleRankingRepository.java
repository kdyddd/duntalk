package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.SaleRankingResponse;
import com.duntalk.domain.item.entity.SaleRanking;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SaleRankingRepository extends JpaRepository<SaleRanking, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.SaleRankingResponse(" +
            "s.item.itemId, s.item.itemName, s.changeRate) " +
            "FROM SaleRanking s " +
            "WHERE s.startTime = :start AND s.changeRate > 0 " +
            "ORDER BY s.changeRate DESC")
    List<SaleRankingResponse> getRisingItems(@Param("start")LocalDateTime start, Pageable pageable);

    @Query("SELECT new com.duntalk.domain.item.dto.SaleRankingResponse(" +
            "s.item.itemId, s.item.itemName, s.changeRate) " +
            "FROM SaleRanking s " +
            "WHERE s.startTime = :start AND s.changeRate < 0 " +
            "ORDER BY s.changeRate ASC")
    List<SaleRankingResponse> getFallingItems(@Param("start")LocalDateTime start, Pageable pageable);

    @Query("SELECT MAX(s.startTime) FROM SaleRanking s")
    Optional<LocalDateTime> findLatestStartTime();

}
