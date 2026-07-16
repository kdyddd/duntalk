package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.ItemResponse;
import com.duntalk.domain.item.dto.NeopleItemDto;
import com.duntalk.domain.item.dto.NeopleItemExplainResponse;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        if(items.isEmpty()) {
            NeopleItemDto dto = neopleApiService.getItem(itemName);
            NeopleItemExplainResponse response = neopleApiService.getItemExplain(dto.getItemId());
            Item newItem = Item.from(dto, response.getItemExplain());
            saveItem(newItem);
            ItemResponse itemResponse = ItemResponse.from(newItem);
            return List.of(itemResponse);
        }
        return items.stream()
                .map(ItemResponse::from)
                .toList();

    }



}
