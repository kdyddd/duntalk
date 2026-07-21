package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.ItemResponse;
import com.duntalk.domain.item.dto.NeopleItemDto;
import com.duntalk.domain.item.dto.NeopleItemExplainResponse;
import com.duntalk.domain.item.dto.NeopleItemResponse;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.entity.SaleHourSummary;
import com.duntalk.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final NeopleApiService neopleApiService;

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<ItemResponse> searchItems(String itemName) {
        List<Item> items = itemRepository.findByItemNameContaining(itemName);

        if (!items.isEmpty()) {
            return items.stream()
                    .map(ItemResponse::from)
                    .toList();
        }

        NeopleItemResponse response = neopleApiService.getItem(itemName);
        List<ItemResponse> itemResponses = new ArrayList<>();

        for (NeopleItemDto dto : response.getRows()) {
            String itemId = dto.getItemId();

            if (itemRepository.existsById(itemId)) {
                continue;
            }

            boolean hasTradeData = neopleApiService.hasAuctionListing(itemId) || neopleApiService.hasSaleListing(itemId);

            if (!hasTradeData) {
                continue;
            }

            NeopleItemExplainResponse explainResponse = neopleApiService.getItemExplain(itemId);

            Item newItem = Item.from(dto, explainResponse.getItemExplain());

            saveItem(newItem);
            itemResponses.add(ItemResponse.from(newItem));
        }

        if (itemResponses.isEmpty()) {
            throw new IllegalArgumentException(
                    "거래 가능한 아이템이 없습니다: " + itemName
            );
        }

        return itemResponses;
    }



}
