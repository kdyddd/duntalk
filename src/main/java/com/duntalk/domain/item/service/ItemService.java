package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.*;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final NeopleItemApiService neopleItemApiService;

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

        NeopleItemResponse response = neopleItemApiService.getItem(itemName);
        List<ItemResponse> itemResponses = new ArrayList<>();

        for (NeopleItemDto dto : response.getRows()) {
            String itemId = dto.getItemId();

            if (itemRepository.existsById(itemId)) {
                continue;
            }

            boolean hasTradeData = neopleItemApiService.hasAuctionListing(itemId) || neopleItemApiService.hasSaleListing(itemId);

            if (!hasTradeData) {
                continue;
            }

            NeopleItemExplainResponse explainResponse = neopleItemApiService.getItemExplain(itemId);

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

    public List<ItemAutocompleteResponse> searchItemAutocomplete(String itemName) {
        List<Item> items = itemRepository.findTop20ByItemNameContaining(itemName);

        return items.stream().map(ItemAutocompleteResponse::from).toList();
    }



}
