package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.*;
import com.duntalk.domain.item.entity.AuctionTenMinuteSummary;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.entity.ItemSaleHistory;
import com.duntalk.domain.item.repository.AuctionTenMinuteSummaryRepository;
import com.duntalk.domain.item.repository.ItemSaleHistoryRepository;
import io.github.resilience4j.ratelimiter.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemHistoryService {

    private final NeopleItemApiService neopleItemApiService;
    private final ItemSaleHistoryRepository itemSaleHistoryRepository;
    private final AuctionTenMinuteSummaryRepository auctionTenMinuteSummaryRepository;
    private final ThreadPoolTaskExecutor apiExecutor;
    private final RateLimiter neopleRateLimiter;

    public void saveAuctionTenMinuteSummary(List<Item> items) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.truncatedTo(ChronoUnit.MINUTES)
                .withMinute(now.getMinute() / 10 * 10);
        AtomicBoolean stopRequested = new AtomicBoolean(false);
        List<CompletableFuture<ItemAuctionApiResult>> futures = new ArrayList<>();
        long apiStart = System.nanoTime();
        for(Item item : items) {
            CompletableFuture<ItemAuctionApiResult> future = CompletableFuture.supplyAsync(
                    () -> {
                        if(stopRequested.get()) {
                            return null;
                        }
                        try {
                            if (!neopleRateLimiter.acquirePermission()) {
                                throw new IllegalStateException("Neople API RateLimiter permission 획득 실패");
                            }

                            if(stopRequested.get()) {
                                return null;
                            }

                            List<NeopleItemAuctionDto> dtoList = neopleItemApiService.getItemAuctionPrice(item.getItemId());
                            return new ItemAuctionApiResult(item, dtoList);
                        } catch (WebClientResponseException e) {
                            NeopleApiErrorResponse errorResponse = e.getResponseBodyAs(NeopleApiErrorResponse.class);
                            String errorCode = null;
                            String errorMessage = null;

                            if (errorResponse != null && errorResponse.error() != null) {
                                errorCode = errorResponse.error().code();
                                errorMessage = errorResponse.error().message();
                            }

                            if ("DNF980".equals(errorCode)
                                    || "API002".equals(errorCode)
                                    || "API008".equals(errorCode)) {
                                log.error("네오플 API 전체 중단 오류 itemId={}, status={}, code={}, message={}, body={}",
                                        item.getItemId(),
                                        e.getStatusCode().value(),
                                        errorCode,
                                        errorMessage,
                                        e.getResponseBodyAsString()
                                );
                                stopRequested.set(true);
                                throw e;
                            }

                            log.warn("개별 네오플 API AuctionSummary 호출 실패 itemId={}, status={}, code={}, message={}, body={}",
                                    item.getItemId(),
                                    e.getStatusCode().value(),
                                    errorCode,
                                    errorMessage,
                                    e.getResponseBodyAsString()
                            );

                            return null;
                        } catch (WebClientRequestException e) {
                            stopRequested.set(true);
                            throw e;
                        }

                    },
                    apiExecutor
            );
            futures.add(future);
        }
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof WebClientResponseException webClientResponseException) {
                throw webClientResponseException;
            }

            if (e.getCause() instanceof WebClientRequestException webClientRequestException) {
                throw webClientRequestException;
            }

            throw e;
        }
        long apiEnd = System.nanoTime();
        for(CompletableFuture<ItemAuctionApiResult> future : futures) {
            ItemAuctionApiResult result = future.join();
            if (result == null) {
                continue;
            }
            List<NeopleItemAuctionDto> dtoList = result.auctions();
            Item item = result.item();

            if(!dtoList.isEmpty()) {
                int minPrice = dtoList.get(0).getUnitPrice();
                long count = 0;
                for(NeopleItemAuctionDto dto : dtoList) {
                    count += dto.getCount();
                }
                AuctionSummaryDto summaryDto = new AuctionSummaryDto(item, minPrice, count);
                auctionTenMinuteSummaryRepository.save(AuctionTenMinuteSummary.from(summaryDto, start));
            }
        }
        long dbEnd = System.nanoTime();
        log.info("[Auction 시간 분석] api={}ms, db={}ms",
                (apiEnd - apiStart) / 1_000_000,
                (dbEnd - apiEnd) / 1_000_000);
    }

    public void saveAllItemSaleHistory(List<Item> items) {
        AtomicBoolean stopRequested = new AtomicBoolean(false);

        List<CompletableFuture<ItemSaleApiResult>> futures = new ArrayList<>();
        long apiStart = System.nanoTime();
        for(Item item : items) {
            CompletableFuture<ItemSaleApiResult> future = CompletableFuture.supplyAsync(
                    () -> {
                        if(stopRequested.get()) {
                            return null;
                        }
                        try {
                            if (!neopleRateLimiter.acquirePermission()) {
                                throw new IllegalStateException("Neople API RateLimiter permission 획득 실패");
                            }

                            if(stopRequested.get()) {
                                return null;
                            }
                            List<NeopleItemSaleDto> dtoList = neopleItemApiService.getItemSalePrice(item.getItemId());
                            return new ItemSaleApiResult(item, dtoList);
                        } catch (WebClientResponseException e) {
                            NeopleApiErrorResponse errorResponse = e.getResponseBodyAs(NeopleApiErrorResponse.class);
                            String errorCode = null;
                            String errorMessage = null;

                            if (errorResponse != null && errorResponse.error() != null) {
                                errorCode = errorResponse.error().code();
                                errorMessage = errorResponse.error().message();
                            }

                            if ("DNF980".equals(errorCode)
                                    || "API002".equals(errorCode)
                                    || "API008".equals(errorCode)) {
                                log.error("네오플 API 전체 중단 오류 itemId={}, status={}, code={}, message={}, body={}",
                                        item.getItemId(),
                                        e.getStatusCode().value(),
                                        errorCode,
                                        errorMessage,
                                        e.getResponseBodyAsString()
                                );
                                stopRequested.set(true);
                                throw e;
                            }

                            log.warn("개별 네오플 API SaleHistory 호출 실패 itemId={}, status={}, code={}, message={}, body={}",
                                    item.getItemId(),
                                    e.getStatusCode().value(),
                                    errorCode,
                                    errorMessage,
                                    e.getResponseBodyAsString()
                            );

                            return null;
                        } catch (WebClientRequestException e) {
                            stopRequested.set(true);
                            throw e;
                        }

                    },
                    apiExecutor
            );
            futures.add(future);
        }
        try {
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } catch (CompletionException e) {
            if (e.getCause() instanceof WebClientResponseException webClientResponseException) {
                throw webClientResponseException;
            }

            if (e.getCause() instanceof WebClientRequestException webClientRequestException) {
                throw webClientRequestException;
            }

            throw e;
        }
        long apiEnd = System.nanoTime();

        for(CompletableFuture<ItemSaleApiResult> future : futures) {
            ItemSaleApiResult result = future.join();
            if (result == null) {
                continue;
            }
            List<NeopleItemSaleDto> dtoList = result.sales();
            Item item = result.item();
            List<ItemSaleHistory> latestItemSaleHistoryList =
                    itemSaleHistoryRepository.findLatestGroupByItem(item);

            if (latestItemSaleHistoryList.isEmpty()) {
                for(NeopleItemSaleDto dto : dtoList) {
                    ItemSaleHistory itemSaleHistory = ItemSaleHistory.from(dto,item);
                    itemSaleHistoryRepository.save(itemSaleHistory);
                }
                continue;
            }

            LocalDateTime latestSoldDate = latestItemSaleHistoryList.get(0).getSoldDate();
            Map<SaleKey, Integer> latestSaleCountMap = new HashMap<>();

            for(ItemSaleHistory itemSaleHistory : latestItemSaleHistoryList) {
                SaleKey saleKey = new SaleKey(
                        itemSaleHistory.getCount(),
                        itemSaleHistory.getPrice()
                );

                latestSaleCountMap.put(
                        saleKey,
                        latestSaleCountMap.getOrDefault(saleKey, 0) + 1
                );
            }

            for(NeopleItemSaleDto dto : dtoList) {
                if(latestSaleCountMap.isEmpty()) {
                    break;
                }

                if (dto.getSoldDate().isAfter(latestSoldDate)) {
                    ItemSaleHistory itemSaleHistory = ItemSaleHistory.from(dto,item);
                    itemSaleHistoryRepository.save(itemSaleHistory);
                    continue;
                }

                if (dto.getSoldDate().isBefore(latestSoldDate)) {
                    break;
                }

                SaleKey saleKey = new SaleKey(dto.getCount(), dto.getPrice());

                if(latestSaleCountMap.containsKey(saleKey)) {
                    Integer count = latestSaleCountMap.get(saleKey);
                    latestSaleCountMap.put(saleKey, count - 1);

                    if (count - 1 == 0) {
                        latestSaleCountMap.remove(saleKey);
                    }

                    continue;
                }

                ItemSaleHistory itemSaleHistory = ItemSaleHistory.from(dto,item);
                itemSaleHistoryRepository.save(itemSaleHistory);
            }
        }
        long dbEnd = System.nanoTime();
        log.info("[Sale 시간 분석] api={}ms, db={}ms",
                (apiEnd - apiStart) / 1_000_000,
                (dbEnd - apiEnd) / 1_000_000);
    }

    @Transactional
    public void deleteRawData() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(2);

        int deleted = itemSaleHistoryRepository.deleteBefore(cutoff);
        log.info("[Raw 데이터 삭제] deleted={}", deleted);
    }

    private record SaleKey(
            int count,
            Long price
    ) {
    }
}
