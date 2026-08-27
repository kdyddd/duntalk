package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.ItemRankingResponse;
import com.duntalk.domain.item.dto.SaleRankingDto;
import com.duntalk.domain.item.dto.SaleRankingResponse;
import com.duntalk.domain.item.entity.SaleRanking;
import com.duntalk.domain.item.repository.SaleDaySummaryRepository;
import com.duntalk.domain.item.repository.SaleHourSummaryRepository;
import com.duntalk.domain.item.repository.SaleRankingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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

    private final SaleHourSummaryRepository saleHourSummaryRepository;
    private final SaleDaySummaryRepository saleDaySummaryRepository;
    private final SaleRankingRepository saleRankingRepository;

    public void updateRanking() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hourStart = now.truncatedTo(ChronoUnit.HOURS).minusHours(1);
        LocalDateTime dayStart = now.toLocalDate().minusDays(1).atStartOfDay();

        List<SaleRankingDto> hourSummaries = saleHourSummaryRepository.findAvgPrices(hourStart);
        List<SaleRankingDto> daySummaries = saleDaySummaryRepository.findAvgPrices(dayStart);

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

        saleRankingRepository.saveAll(rankings);
    }

    public ItemRankingResponse getRankings(int limit) {
        Optional<LocalDateTime> start = saleRankingRepository.findLatestStartTime();

        if(start.isEmpty()) {
            return new ItemRankingResponse(
                    List.of(),
                    List.of()
            );
        }

        LocalDateTime latestStart = start.get();

        List<SaleRankingResponse> risingItems =
                saleRankingRepository.getRisingItems(latestStart, PageRequest.of(0, limit));

        List<SaleRankingResponse> fallingItems =
                saleRankingRepository.getFallingItems(latestStart, PageRequest.of(0, limit));

        return new ItemRankingResponse(
                risingItems,
                fallingItems
        );
    }

}
