package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.NeopleItemAuctionDto;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.entity.ItemAuctionHistory;
import com.duntalk.domain.item.repository.ItemAuctionHistoryRepository;
import com.duntalk.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemHistoryService {

    private final NeopleApiService neopleApiService;
    private final ItemRepository itemRepository;
    private final ItemAuctionHistoryRepository itemAuctionHistoryRepository;

    public void saveItemAuctionHistory(String itemId) {
        List<NeopleItemAuctionDto> dtoList = neopleApiService.getItemAuctionPrice(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("아이템이 없습니다: " + itemId));

        for(NeopleItemAuctionDto dto : dtoList) {
            ItemAuctionHistory itemAuctionHistory = ItemAuctionHistory.from(dto,item);
            itemAuctionHistoryRepository.save(itemAuctionHistory);
        }

    }

}
