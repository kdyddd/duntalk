package com.duntalk.domain.item.scheduler;

import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.repository.ItemRepository;
import com.duntalk.domain.item.service.ItemHistoryService;
import com.duntalk.domain.item.service.ItemRankingService;
import com.duntalk.domain.item.service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemHistoryScheduler {

    private final ItemHistoryService itemHistoryService;
    private final ItemRepository itemRepository;
    private final SummaryService summaryService;
    private final ItemRankingService itemRankingService;

    @Scheduled(cron = "0 0/10 * * * *")
    public void autoSaveAllItemHistory () {
        List<Item> items = itemRepository.findAll();
        itemHistoryService.saveAllItemSaleHistory(items);
        itemHistoryService.saveAuctionTenMinuteSummary(items);
        summaryService.saveSaleTenMinuteSummary();
    }

    @Scheduled(cron = "0 4 * * * *")
    public void autoSaveHourSummary () {
        itemRankingService.updateRanking();
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


}
