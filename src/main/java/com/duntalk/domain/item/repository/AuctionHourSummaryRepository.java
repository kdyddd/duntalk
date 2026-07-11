package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.entity.AuctionHourSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionHourSummaryRepository extends JpaRepository<AuctionHourSummary, Long> {
}
