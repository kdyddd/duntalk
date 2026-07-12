package com.duntalk.domain.item.scheduler;

import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.repository.ItemRepository;
import com.duntalk.domain.item.service.ItemHistoryService;
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

    @Scheduled(cron = "0 0/10 * * * *")
    public void autoSaveAllItemHistory () {
        List<Item> items = itemRepository.findAll();
        itemHistoryService.saveAllItemSaleHistory(items);
        itemHistoryService.saveAuctionTenMinuteSummary(items);
    }


}
