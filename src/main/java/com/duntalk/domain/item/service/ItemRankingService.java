package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.ItemRankingResponse;
import com.duntalk.domain.item.dto.SaleRankingDto;
import com.duntalk.domain.item.dto.SaleRankingResponse;
import com.duntalk.domain.item.entity.SaleRanking;
import com.duntalk.domain.item.repository.SaleDaySummaryRepository;
import com.duntalk.domain.item.repository.SaleHourSummaryRepository;
import com.duntalk.domain.item.repository.SaleRankingRepository;
import com.duntalk.domain.item.repository.SaleTenMinuteSummaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRankingService {

    private final SaleTenMinuteSummaryRepository saleTenMinuteSummaryRepository;
    private final SaleDaySummaryRepository saleDaySummaryRepository;
    private final SaleRankingRepository saleRankingRepository;

    @Transactional
    @CacheEvict(cacheNames = "itemRankings", allEntries = true, condition = "#result == true")
    public boolean updateRanking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime end = now.truncatedTo(ChronoUnit.MINUTES);
        end = end.withMinute(end.getMinute() / 10 * 10);
        LocalDateTime tenMinuteStart = end.minusMinutes(10);
        LocalDateTime dayStart = now.toLocalDate().minusDays(1).atStartOfDay();

        List<SaleRankingDto> tenMinuteSummaries = saleTenMinuteSummaryRepository.findAvgPrices(tenMinuteStart);
        List<SaleRankingDto> daySummaries = saleDaySummaryRepository.findAvgPrices(dayStart);

        if (tenMinuteSummaries.isEmpty() || daySummaries.isEmpty()) {
            return false;
        }

        Map<String, Integer> daySummaryMap =
                daySummaries.stream()
                        .collect(Collectors.toMap(
                                dto -> dto.getItem().getItemId(),
                                SaleRankingDto::getAvgPrice
                        ));
        List<SaleRanking> rankings = new ArrayList<>();

        for(SaleRankingDto tenMinuteSummary : tenMinuteSummaries) {
            Integer dayAvgPrice = daySummaryMap.get(tenMinuteSummary.getItem().getItemId());

            if(dayAvgPrice == null || dayAvgPrice == 0) {
                continue;
            }

            double changeRate = ((double) tenMinuteSummary.getAvgPrice() - dayAvgPrice) / dayAvgPrice * 100;

            rankings.add(SaleRanking.create(tenMinuteSummary.getItem(), changeRate, tenMinuteStart));

        }
        if (rankings.isEmpty()) {
            return false;
        }

        saleRankingRepository.deleteAllInBatch();
        saleRankingRepository.saveAll(rankings);
        return true;
    }

    @Cacheable(cacheNames = "itemRankings", key = "#limit")
    public ItemRankingResponse getRankings(int limit) {

        List<SaleRankingResponse> risingItems =
                saleRankingRepository.getRisingItems(PageRequest.of(0, limit));

        List<SaleRankingResponse> fallingItems =
                saleRankingRepository.getFallingItems(PageRequest.of(0, limit));

        return new ItemRankingResponse(
                risingItems,
                fallingItems
        );
    }

}
