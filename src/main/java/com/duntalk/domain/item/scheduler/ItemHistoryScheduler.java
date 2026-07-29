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
                return;
            }

            log.error(
                    "[10분 수집 실패] 네오플 API 오류, status={}, message={}, total={}ms",
                    status,
                    e.getMessage(),
                    totalMs,
                    e
            );

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
    }

    @Scheduled(cron = "0 4 * * * *")
    public void autoSaveHourSummary () {
        summaryService.saveSaleHourSummary();
        summaryService.saveAuctionHourSummary();
    }

    @Scheduled(cron = "0 5 0 * * *")
    public void autoSaveDaySummary () {
        summaryService.saveSaleDaySummary();
        summaryService.saveAuctionDaySummary();
    }

    @Scheduled(cron = "0 6 * * * *")
    public void autoUpdateRanking () {
        itemRankingService.updateRanking();
    }

    @Scheduled(cron = "0 0 4 * * MON")
    public void autoSaveWeekSummary () {
        summaryService.saveSaleWeekSummary();
        summaryService.saveAuctionWeekSummary();
    }

    private long elapsedMillis(long startTime) {
        return (System.nanoTime() - startTime) / 1_000_000;
    }


}
