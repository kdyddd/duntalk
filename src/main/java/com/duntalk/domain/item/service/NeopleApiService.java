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

    public NeopleItemResponse getItem(String itemName) {
        return webClient.get().uri(uriBuilder -> uriBuilder
                .path("/df/auction")
                .queryParam("itemName", itemName)
                .queryParam("limit", 1)
                .queryParam("apikey", apiKey)
                .build())
                .retrieve().bodyToMono(NeopleItemResponse.class)
                .block();

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
