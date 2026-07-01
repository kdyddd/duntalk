package com.duntalk.domain.item.scheduler;

import com.duntalk.domain.item.service.ItemHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ItemHistoryScheduler {

    private final ItemHistoryService itemHistoryService;

    @Scheduled(cron = "0 0/10 * * * *")
    public void autoSaveAllItemHistory () {
        itemHistoryService.saveAllItemHistory();
    }


}
