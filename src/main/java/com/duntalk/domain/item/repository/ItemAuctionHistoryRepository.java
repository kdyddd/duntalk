package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.entity.ItemAuctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemAuctionHistoryRepository extends JpaRepository<ItemAuctionHistory, Long> {
}
