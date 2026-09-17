package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.ItemRankingResponse;
import com.duntalk.domain.item.dto.SaleRankingDto;
import com.duntalk.domain.item.dto.RankedItemResponse;
import com.duntalk.domain.item.entity.SaleRanking;
import com.duntalk.domain.item.repository.SaleDaySummaryRepository;
import com.duntalk.domain.item.repository.SaleHourSummaryRepository;
import com.duntalk.domain.item.repository.SaleRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRankingService {

    private final SaleHourSummaryRepository saleHourSummaryRepository;
    private final SaleDaySummaryRepository saleDaySummaryRepository;
    private final SaleRankingRepository saleRankingRepository;

    private final StringRedisTemplate stringRedisTemplate;

    @Transactional
    @CacheEvict(cacheNames = "itemRankings", allEntries = true)
    public void updateRanking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourStart = now.truncatedTo(ChronoUnit.HOURS).minusHours(1);
        LocalDateTime dayStart = now.toLocalDate().minusDays(1).atStartOfDay();

        List<SaleRankingDto> hourSummaries = saleHourSummaryRepository.findAvgPrices(hourStart);
        List<SaleRankingDto> daySummaries = saleDaySummaryRepository.findAvgPrices(dayStart);

        if (hourSummaries.isEmpty() || daySummaries.isEmpty()) {
            return;
        }

        Map<String, Integer> daySummaryMap =
                daySummaries.stream()
                        .collect(Collectors.toMap(
                                dto -> dto.getItem().getItemId(),
                                SaleRankingDto::getAvgPrice
                        ));
        List<SaleRanking> rankings = new ArrayList<>();

        for(SaleRankingDto hourSummary : hourSummaries) {
            Integer dayAvgPrice = daySummaryMap.get(hourSummary.getItem().getItemId());

            if(dayAvgPrice == null || dayAvgPrice == 0) {
                continue;
            }

            double changeRate = ((double) hourSummary.getAvgPrice() - dayAvgPrice) / dayAvgPrice * 100;

            rankings.add(SaleRanking.create(hourSummary.getItem(), changeRate, hourStart));

        }

        saleRankingRepository.deleteAllInBatch();
        saleRankingRepository.saveAll(rankings);
    }

    @Cacheable(cacheNames = "itemRankings", key = "#limit")
    public ItemRankingResponse getRankings(int limit) {

        List<RankedItemResponse> risingItems =
                saleRankingRepository.getRisingItems(PageRequest.of(0, limit));

        List<RankedItemResponse> fallingItems =
                saleRankingRepository.getFallingItems(PageRequest.of(0, limit));

        return new ItemRankingResponse(
                risingItems,
                fallingItems
        );
    }

    public List<RankedItemResponse> getPopularItems(int limit) {
        Set<String> itemIds = stringRedisTemplate.opsForZSet()
                .reverseRange("item:ranking:popular", 0, limit - 1);

        if (itemIds == null || itemIds.isEmpty()) {
            return List.of();
        }

        List<RankedItemResponse> popularItems = saleRankingRepository.getPopularItems(itemIds);

        Map<String, RankedItemResponse> popularItemMap = popularItems.stream()
                .collect(Collectors.toMap(
                        RankedItemResponse::getItemId,
                        item -> item
                ));

        return itemIds.stream()
                .map(popularItemMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public void increaseItemViewCount(String itemId) {
        stringRedisTemplate.opsForZSet()
                .incrementScore("item:ranking:popular", itemId, 1);
    }

}
