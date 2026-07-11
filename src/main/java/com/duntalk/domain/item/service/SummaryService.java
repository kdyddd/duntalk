package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import com.duntalk.domain.item.dto.SaleSummaryDto;
import com.duntalk.domain.item.entity.AuctionHourSummary;
import com.duntalk.domain.item.entity.SaleTenMinuteSummary;
import com.duntalk.domain.item.repository.AuctionHourSummaryRepository;
import com.duntalk.domain.item.repository.ItemAuctionHistoryRepository;
import com.duntalk.domain.item.repository.ItemSaleHistoryRepository;
import com.duntalk.domain.item.repository.SaleTenMinuteSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final ItemAuctionHistoryRepository itemAuctionHistoryRepository;
    private final AuctionHourSummaryRepository auctionHourSummaryRepository;
    private final ItemSaleHistoryRepository itemSaleHistoryRepository;
    private final SaleTenMinuteSummaryRepository saleTenMinuteSummaryRepository;


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

}
