package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.entity.AuctionWeekSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuctionWeekRepository extends JpaRepository<AuctionWeekSummary, Long> {
}
