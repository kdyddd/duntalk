package com.duntalk.domain.item.controller;

import com.duntalk.domain.item.dto.ItemResponse;
import com.duntalk.domain.item.dto.ItemStatisticsResponse;
import com.duntalk.domain.item.service.ItemHistoryService;
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

    @GetMapping("/items/{itemName}")
    public List<ItemResponse> searchItems(@PathVariable String itemName) {

        return itemService.searchItems(itemName);
    }

    @GetMapping("/items/{itemId}/statistics")
    public ItemStatisticsResponse getItemStatistics(@PathVariable String itemId, @RequestParam(defaultValue = "TEN_MINUTES") StatisticsInterval interval) {
        return itemStatisticsService.getItemStatistics(itemId, interval);
    }


}
