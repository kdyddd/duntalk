package com.duntalk.domain.item.controller;

import com.duntalk.domain.item.dto.ItemResponse;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.service.ItemService;
import com.duntalk.domain.item.service.NeopleApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/{itemName}")
    public List<ItemResponse> searchItems(@PathVariable String itemName) {

        return itemService.searchItems(itemName);
    }



}
