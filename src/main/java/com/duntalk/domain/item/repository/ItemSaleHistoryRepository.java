package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.entity.ItemSaleHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemSaleHistoryRepository extends JpaRepository<ItemSaleHistory, Long> {
}
