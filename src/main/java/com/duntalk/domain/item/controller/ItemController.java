package com.duntalk.domain.item.controller;

import com.duntalk.domain.item.dto.ItemResponse;
import com.duntalk.domain.item.service.ItemHistoryService;
import com.duntalk.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemHistoryService itemHistoryService;

    @GetMapping("/items/{itemName}")
    public List<ItemResponse> searchItems(@PathVariable String itemName) {

        return itemService.searchItems(itemName);
    }

    @GetMapping("/auction/{itemId}")
    public void saveItemAuctionHistory(@PathVariable String itemId) {
        itemHistoryService.saveItemAuctionHistory(itemId);
    }

    @GetMapping("/sale/{itemId}")
    public void saveItemSaleHistory(@PathVariable String itemId) {
        itemHistoryService.saveItemSaleHistory(itemId);
    }

}
