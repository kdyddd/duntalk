package com.duntalk.domain.item.scheduler;

import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.repository.ItemRepository;
import com.duntalk.domain.item.service.ItemHistoryService;
import com.duntalk.domain.item.service.ItemRankingService;
import com.duntalk.domain.item.service.SummaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemHistoryScheduler {

    private final ItemHistoryService itemHistoryService;
    private final ItemRepository itemRepository;
    private final SummaryService summaryService;
    private final ItemRankingService itemRankingService;

    @Scheduled(cron = "0 0/10 * * * *")
    public void autoSaveAllItemHistory() {
        LocalDateTime cycleTime = LocalDateTime.now();
        long totalStart = System.nanoTime();

        try {
            long itemLoadStart = System.nanoTime();
            List<Item> items = itemRepository.findAll();
            long itemLoadMs = elapsedMillis(itemLoadStart);

            long saleStart = System.nanoTime();
            itemHistoryService.saveAllItemSaleHistory(items);
            long saleMs = elapsedMillis(saleStart);

            long auctionStart = System.nanoTime();
            itemHistoryService.saveAuctionTenMinuteSummary(items);
            long auctionMs = elapsedMillis(auctionStart);

            long summaryStart = System.nanoTime();
            summaryService.saveSaleTenMinuteSummary();
            long summaryMs = elapsedMillis(summaryStart);

            long totalMs = elapsedMillis(totalStart);

            log.info(
                    "[10분 수집 완료] items={}, itemLoad={}ms, sale={}ms, auction={}ms, summary={}ms, total={}ms",
                    items.size(),
                    itemLoadMs,
                    saleMs,
                    auctionMs,
                    summaryMs,
                    totalMs
            );

        } catch (WebClientResponseException e) {
            long totalMs = elapsedMillis(totalStart);
            int status = e.getStatusCode().value();

            if (status == 503) {
                log.warn(
                        "[10분 수집 중단] 네오플 API 점검 중, status=503, total={}ms",
                        totalMs
                );
            } else {
                log.error(
                        "[10분 수집 실패] 네오플 API 오류, status={}, message={}, total={}ms",
                        status,
                        e.getMessage(),
                        totalMs,
                        e
                );
            }


        } catch (WebClientRequestException e) {
            log.warn(
                    "[10분 수집 중단] 네오플 API 연결 또는 응답 실패, message={}, total={}ms",
                    e.getMessage(),
                    elapsedMillis(totalStart)
            );

        } catch (Exception e) {
            log.error(
                    "[10분 수집 실패] 예상하지 못한 오류, message={}, total={}ms",
                    e.getMessage(),
                    elapsedMillis(totalStart),
                    e
            );
        }

        if (cycleTime.getMinute() == 0) {
            long start = System.nanoTime();

            try {
                summaryService.saveSaleHourSummary();
                summaryService.saveAuctionHourSummary();

                log.info("[시간 통계 완료] total={}ms", elapsedMillis(start));
            } catch (Exception e) {
                log.error(
                        "[시간 통계 실패] message={}, total={}ms",
                        e.getMessage(),
                        elapsedMillis(start),
                        e
                );
            }
        }

        if (cycleTime.getHour() == 0 && cycleTime.getMinute() == 0) {
            long start = System.nanoTime();

            try {
                summaryService.saveSaleDaySummary();
                summaryService.saveAuctionDaySummary();

                log.info("[일간 통계 완료] total={}ms", elapsedMillis(start));
            } catch (Exception e) {
                log.error(
                        "[일간 통계 실패] message={}, total={}ms",
                        e.getMessage(),
                        elapsedMillis(start),
                        e
                );
            }
        }

        long rankingStart = System.nanoTime();

        try {
            itemRankingService.updateRanking();

            log.info("[랭킹 처리 완료] total={}ms", elapsedMillis(rankingStart));
        } catch (Exception e) {
            log.error(
                    "[랭킹 처리 실패] message={}, total={}ms",
                    e.getMessage(),
                    elapsedMillis(rankingStart),
                    e
            );
        }

        if (cycleTime.getDayOfWeek() == DayOfWeek.MONDAY && cycleTime.getHour() == 4 && cycleTime.getMinute() == 0) {
            long start = System.nanoTime();

            try {
                summaryService.saveSaleWeekSummary();
                summaryService.saveAuctionWeekSummary();
                log.info("[주간 통계 완료] total={}ms", elapsedMillis(start));
            } catch (Exception e) {
                log.error(
                        "[주간 통계 실패] message={}, total={}ms",
                        e.getMessage(),
                        elapsedMillis(start),
                        e
                );
            }
        }
    }

    @Scheduled(cron = "0 44 5 * * *")
    public void autoDeleteOldData() {
        long start = System.nanoTime();
        summaryService.deleteSummary();
        itemHistoryService.deleteRawData();
        log.info("[데이터 삭제 완료] total={}ms", elapsedMillis(start));
    }

    private long elapsedMillis(long startTime) {
        return (System.nanoTime() - startTime) / 1_000_000;
    }


}
