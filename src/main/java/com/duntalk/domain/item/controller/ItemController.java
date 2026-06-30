package com.duntalk.domain.item.controller;

import com.duntalk.domain.item.dto.NeopleItemResponse;
import com.duntalk.domain.item.service.NeopleApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final NeopleApiService neopleApiService;

    @GetMapping("/{itemName}")
    public NeopleItemResponse searchItem(@PathVariable String itemName) {

        return neopleApiService.getItem(itemName);

    }



}
