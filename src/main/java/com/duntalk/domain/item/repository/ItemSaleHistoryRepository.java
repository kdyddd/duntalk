package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import com.duntalk.domain.item.dto.SaleSummaryDto;
import com.duntalk.domain.item.entity.ItemSaleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ItemSaleHistoryRepository extends JpaRepository<ItemSaleHistory, Long> {
    @Query("SELECT new com.duntalk.domain.item.dto.SaleSummaryDto(" +
            "h.item, SUM(h.price * h.count *1L), SUM(h.count)) " +
            "FROM ItemSaleHistory h " +
            "WHERE h.soldDate >= :start AND h.soldDate < :end " +
            "GROUP BY h.item")
    List<SaleSummaryDto> findSaleSummary(@Param("start") LocalDateTime start,
                                         @Param("end") LocalDateTime end);

}
