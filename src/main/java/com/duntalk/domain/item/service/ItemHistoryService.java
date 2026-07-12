package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.AuctionSummaryDto;
import com.duntalk.domain.item.dto.NeopleItemAuctionDto;
import com.duntalk.domain.item.dto.NeopleItemSaleDto;
import com.duntalk.domain.item.entity.AuctionTenMinuteSummary;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.entity.ItemSaleHistory;
import com.duntalk.domain.item.repository.AuctionTenMinuteSummaryRepository;
import com.duntalk.domain.item.repository.ItemSaleHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemHistoryService {

    private final NeopleApiService neopleApiService;
    private final ItemSaleHistoryRepository itemSaleHistoryRepository;
    private final AuctionTenMinuteSummaryRepository auctionTenMinuteSummaryRepository;

    public void saveItemSaleHistory(Item item) {
        List<NeopleItemSaleDto> dtoList = neopleApiService.getItemSalePrice(item.getItemId());

        for(NeopleItemSaleDto dto : dtoList) {
            ItemSaleHistory itemSaleHistory = ItemSaleHistory.from(dto,item);
            itemSaleHistoryRepository.save(itemSaleHistory);
        }

    }

    public void saveAuctionTenMinuteSummary(List<Item> items) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.truncatedTo(ChronoUnit.MINUTES)
                .withMinute(now.getMinute() / 10 * 10);
        for(Item item : items) {
            List<NeopleItemAuctionDto> dtoList = neopleApiService.getItemAuctionPrice(item.getItemId());
            if(!dtoList.isEmpty()) {
                int minPrice = dtoList.get(0).getUnitPrice();
                long count = 0;
                for(NeopleItemAuctionDto dto : dtoList) {
                    count += dto.getCount();
                }
                AuctionSummaryDto summaryDto = new AuctionSummaryDto(item, minPrice, count);
                auctionTenMinuteSummaryRepository.save(AuctionTenMinuteSummary.from(summaryDto, start));
            }
        }
    }

    public void saveAllItemSaleHistory(List<Item> items) {
        for(Item item : items) {
            saveItemSaleHistory(item);
        }
    }

}
