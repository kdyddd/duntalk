package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.NeopleItemDto;
import com.duntalk.domain.item.dto.NeopleItemPriceResponse;
import com.duntalk.domain.item.dto.NeopleItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

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

    public NeopleItemPriceResponse getItemPrice(String itemName) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .path("/df/auction")
                .queryParam("itemName", itemName)
                .queryParam("apikey", apiKey)
                .build())
                .retrieve().bodyToMono(NeopleItemPriceResponse.class)
                .block();

    }

}
