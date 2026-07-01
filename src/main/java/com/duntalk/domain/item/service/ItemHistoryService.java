package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.NeopleItemAuctionDto;
import com.duntalk.domain.item.dto.NeopleItemSaleDto;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.entity.ItemAuctionHistory;
import com.duntalk.domain.item.entity.ItemSaleHistory;
import com.duntalk.domain.item.repository.ItemAuctionHistoryRepository;
import com.duntalk.domain.item.repository.ItemRepository;
import com.duntalk.domain.item.repository.ItemSaleHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemHistoryService {

    private final NeopleApiService neopleApiService;
    private final ItemRepository itemRepository;
    private final ItemAuctionHistoryRepository itemAuctionHistoryRepository;
    private final ItemSaleHistoryRepository itemSaleHistoryRepository;

    public void saveItemAuctionHistory(String itemId) {
        List<NeopleItemAuctionDto> dtoList = neopleApiService.getItemAuctionPrice(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템이 없습니다: " + itemId));

        for(NeopleItemAuctionDto dto : dtoList) {
            ItemAuctionHistory itemAuctionHistory = ItemAuctionHistory.from(dto,item);
            itemAuctionHistoryRepository.save(itemAuctionHistory);
        }

    }

    public void saveItemSaleHistory(String itemId) {
        List<NeopleItemSaleDto> dtoList = neopleApiService.getItemSalePrice(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템이 없습니다: " + itemId));

        for(NeopleItemSaleDto dto : dtoList) {
            ItemSaleHistory itemSaleHistory = ItemSaleHistory.from(dto,item);
            itemSaleHistoryRepository.save(itemSaleHistory);
        }

    }

    public void saveAllItemHistory() {
        List<Item> items = itemRepository.findAll();
        for(Item item : items) {
            saveItemSaleHistory(item.getItemId());
            saveItemAuctionHistory(item.getItemId());
        }
    }

}
