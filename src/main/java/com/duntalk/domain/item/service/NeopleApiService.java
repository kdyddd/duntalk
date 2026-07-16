package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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

    public NeopleItemExplainResponse getItemExplain(String itemId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/df/items/{itemId}")
                        .queryParam("apikey", apiKey)
                        .build(itemId))
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        response -> Mono.error(
                                new IllegalArgumentException(
                                        "아이템 상세정보가 없습니다: " + itemId
                                )
                        )
                )
                .bodyToMono(NeopleItemExplainResponse.class)
                .blockOptional()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "아이템 상세정보 응답이 비어 있습니다: " + itemId
                        )
                );
    }

    public List<NeopleItemAuctionDto> getItemAuctionPrice(String itemId) {
        NeopleItemAuctionResponse response = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/df/auction")
                .queryParam("itemId", itemId)
                .queryParam("limit", 400)
                .queryParam("unitPrice", "asc")
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
                        .queryParam("limit", 100)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve().bodyToMono(NeopleItemSaleResponse.class)
                .block();

        return response.getRows();
    }

}
