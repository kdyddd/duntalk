package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NeopleApiService {

    private final WebClient webClient;

    @Value("${neople.api-key}")
    private String apiKey;

    public NeopleItemDto getItem(String itemName) {
        NeopleItemResponse response = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/df/auction")
                .queryParam("itemName", itemName)
                .queryParam("limit", 1)
                .queryParam("apikey", apiKey)
                .build())
                .retrieve().bodyToMono(NeopleItemResponse.class)
                .block();

        if(response.getRows().isEmpty()) {
            throw new IllegalArgumentException("검색 결과가 없습니다: " + itemName);
        }

        return response.getRows().get(0);
    }

    public List<NeopleItemAuctionDto> getItemAuctionPrice(String itemId) {
        NeopleItemAuctionResponse response = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/df/auction")
                .queryParam("itemId", itemId)
                .queryParam("apikey", apiKey)
                .build())
                .retrieve().bodyToMono(NeopleItemAuctionResponse.class)
                .block();

        return response.getRows();
    }

    public List<NeopleItemSaleDto> getItemSalePrice(String itemId) {
        NeopleItemSaleResponse response = webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/df/auction-sold")
                        .queryParam("itemId", itemId)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve().bodyToMono(NeopleItemSaleResponse.class)
                .block();

        return response.getRows();
    }

}
