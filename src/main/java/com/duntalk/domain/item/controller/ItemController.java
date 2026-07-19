package com.duntalk.domain.item.controller;

import com.duntalk.domain.item.dto.ItemRankingResponse;
import com.duntalk.domain.item.dto.ItemResponse;
import com.duntalk.domain.item.dto.ItemStatisticsResponse;
import com.duntalk.domain.item.service.ItemRankingService;
import com.duntalk.domain.item.service.ItemService;
import com.duntalk.domain.item.service.ItemStatisticsService;
import com.duntalk.domain.item.type.StatisticsInterval;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemStatisticsService itemStatisticsService;
    private final ItemRankingService itemRankingService;

    @GetMapping("/items")
    public List<ItemResponse> searchItems(@RequestParam String itemName) {

        return itemService.searchItems(itemName);
    }

    @GetMapping("/items/{itemId}/statistics")
    public ItemStatisticsResponse getItemStatistics(@PathVariable String itemId, @RequestParam(defaultValue = "TEN_MINUTES") StatisticsInterval interval) {
        return itemStatisticsService.getItemStatistics(itemId, interval);
    }

    @GetMapping("/items/rankings")
    public ItemRankingResponse getRankings(@RequestParam(defaultValue = "5") int limit) {

        return itemRankingService.getRankings(limit);

    }


}
