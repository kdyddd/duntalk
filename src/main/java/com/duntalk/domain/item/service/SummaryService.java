package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import com.duntalk.domain.item.dto.SaleSummaryDto;
import com.duntalk.domain.item.entity.AuctionHourSummary;
import com.duntalk.domain.item.entity.SaleDaySummary;
import com.duntalk.domain.item.entity.SaleHourSummary;
import com.duntalk.domain.item.entity.SaleTenMinuteSummary;
import com.duntalk.domain.item.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final ItemAuctionHistoryRepository itemAuctionHistoryRepository;
    private final AuctionHourSummaryRepository auctionHourSummaryRepository;
    private final ItemSaleHistoryRepository itemSaleHistoryRepository;
    private final SaleTenMinuteSummaryRepository saleTenMinuteSummaryRepository;
    private final SaleHourSummaryRepository saleHourSummaryRepository;
    private final SaleDaySummaryRepository saleDaySummaryRepository;
    private final SaleWeekSummaryRepository saleWeekSummaryRepository;


    public void saveAuctionHourly() {
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime start = end.minusHours(1);
        List<AuctionSummaryDto> dtoList= itemAuctionHistoryRepository.findAuctionStats(start, end);
        for (AuctionSummaryDto dto : dtoList) {
            auctionHourSummaryRepository.save(AuctionHourSummary.from(dto, start));
        }
    }

    public void saveSaleTenMinuteSummary() {
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        end = end.withMinute(end.getMinute() / 10 * 10);
        LocalDateTime start = end.minusMinutes(10);
        List<SaleSummaryDto> dtoList = itemSaleHistoryRepository.findSaleSummary(start, end);
        for (SaleSummaryDto dto : dtoList) {
            saleTenMinuteSummaryRepository.save(SaleTenMinuteSummary.from(dto, start));
        }
    }

    public void saveSaleHourSummary() {
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
        LocalDateTime start = end.minusHours(1);
        List<SaleSummaryDto> dtoList = saleTenMinuteSummaryRepository.findHourRollup(start, end);
        for (SaleSummaryDto dto : dtoList) {
            saleHourSummaryRepository.save(SaleHourSummary.from(dto, start));
        }
    }

    public void saveSaleDaySummary() {
        LocalDateTime end = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
        LocalDateTime start = end.minusDays(1);
        List<SaleSummaryDto> dtoList = saleHourSummaryRepository.findDayRollup(start, end);
        for (SaleSummaryDto dto : dtoList) {
            saleDaySummaryRepository.save(SaleDaySummary.from(dto, start));
        }
    }

    public void saveSaleWeekSummary() {
        LocalDate thisMonday = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDateTime end = thisMonday.atStartOfDay();
        LocalDateTime start = end.minusWeeks(1);
        List<SaleSummaryDto> dtoList = saleHourSummaryRepository.findDayRollup(start, end);
        for (SaleSummaryDto dto : dtoList) {
            saleDaySummaryRepository.save(SaleDaySummary.from(dto, start));
        }
    }

}
