package com.duntalk.domain.item.dto;

import com.duntalk.domain.item.type.StatisticsInterval;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemStatisticsResponse {
    private List<SaleSummaryResponse> sales;
    private List<AuctionSummaryResponse> auctions;
}
