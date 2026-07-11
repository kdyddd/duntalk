package com.duntalk.domain.item.repository;

import com.duntalk.domain.item.entity.SaleWeekSummary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleWeekSummaryRepository extends JpaRepository<SaleWeekSummary, Long> {
}
