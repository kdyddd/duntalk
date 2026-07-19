package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.AuctionSummaryResponse;
import com.duntalk.domain.item.dto.ItemStatisticsResponse;
import com.duntalk.domain.item.dto.SaleSummaryResponse;
import com.duntalk.domain.item.repository.*;
import com.duntalk.domain.item.type.StatisticsInterval;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemStatisticsService {

    private final SaleTenMinuteSummaryRepository saleTenMinuteSummaryRepository;
    private final SaleHourSummaryRepository saleHourSummaryRepository;
    private final SaleDaySummaryRepository saleDaySummaryRepository;
    private final SaleWeekSummaryRepository saleWeekSummaryRepository;

    private final AuctionTenMinuteSummaryRepository auctionTenMinuteSummaryRepository;
    private final AuctionHourSummaryRepository auctionHourSummaryRepository;
    private final AuctionDaySummaryRepository auctionDaySummaryRepository;
    private final AuctionWeekSummaryRepository auctionWeekSummaryRepository;

    public ItemStatisticsResponse getItemStatistics (String itemId, StatisticsInterval interval) {
        return switch (interval) {
            case TEN_MINUTES -> getTenMinuteItemStatistics(itemId);
            case HOUR -> getHourItemStatistics(itemId);
            case DAY -> getDayItemStatistics(itemId);
            case WEEK -> getWeekItemStatistics(itemId);
        };
    }


    private ItemStatisticsResponse getTenMinuteItemStatistics (String itemId) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(1);
        List<SaleSummaryResponse> saleSummaries = saleTenMinuteSummaryRepository.findSaleTenMinuteSummaries(itemId, start, end);
        List<AuctionSummaryResponse> auctionSummaries = auctionTenMinuteSummaryRepository.findAuctionTenMinuteSummaries(itemId, start, end);
        return new ItemStatisticsResponse(saleSummaries, auctionSummaries);
    }

    private ItemStatisticsResponse getHourItemStatistics (String itemId) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(3);
        List<SaleSummaryResponse> saleSummaries = saleHourSummaryRepository.findSaleHourSummaries(itemId, start, end);
        List<AuctionSummaryResponse> auctionSummaries = auctionHourSummaryRepository.findAuctionHourSummaries(itemId, start, end);
        return new ItemStatisticsResponse(saleSummaries, auctionSummaries);
    }

    private ItemStatisticsResponse getDayItemStatistics (String itemId) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusDays(150);
        List<SaleSummaryResponse> saleSummaries = saleDaySummaryRepository.findSaleDaySummaries(itemId, start, end);
        List<AuctionSummaryResponse> auctionSummaries = auctionDaySummaryRepository.findAuctionDaySummaries(itemId, start, end);
        return new ItemStatisticsResponse(saleSummaries, auctionSummaries);
    }

    private ItemStatisticsResponse getWeekItemStatistics (String itemId) {
        LocalDateTime end = LocalDateTime.now();
        LocalDateTime start = end.minusWeeks(50);
        List<SaleSummaryResponse> saleSummaries = saleWeekSummaryRepository.findSaleWeekSummaries(itemId, start, end);
        List<AuctionSummaryResponse> auctionSummaries = auctionWeekSummaryRepository.findAuctionWeekSummaries(itemId, start, end);
        return new ItemStatisticsResponse(saleSummaries, auctionSummaries);
    }





}
