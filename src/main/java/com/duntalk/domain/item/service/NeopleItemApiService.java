package com.duntalk.domain.item.service;

import com.duntalk.domain.item.dto.*;
import com.duntalk.global.exception.ExternalApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NeopleItemApiService {

    private final WebClient webClient;

    @Value("${neople.api-key}")
    private String apiKey;

    public boolean hasAuctionListing(String itemId) {
        NeopleItemResponse response = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/df/auction")
                .queryParam("itemId", itemId)
                .queryParam("limit", 1)
                .queryParam("apikey", apiKey)
                .build())
                .retrieve().bodyToMono(NeopleItemResponse.class)
                .blockOptional()
                .orElseThrow(() ->
                        new ExternalApiException("네오플 API 응답이 비어 있습니다."));

        if(response.getRows() == null) {
            throw new ExternalApiException("네오플 API 응답 내부 rows가 비어 있습니다.");
        }

        return !response.getRows().isEmpty();
    }

    public boolean hasSaleListing(String itemId) {
        NeopleItemResponse response = webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/df/auction-sold")
                        .queryParam("itemId", itemId)
                        .queryParam("limit", 1)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve().bodyToMono(NeopleItemResponse.class)
                .blockOptional()
                .orElseThrow(() ->
                        new ExternalApiException("네오플 API 응답이 비어 있습니다."));

        if(response.getRows() == null) {
            throw new ExternalApiException("네오플 API 응답 내부 rows가 비어 있습니다.");
        }

        return !response.getRows().isEmpty();
    }

    public NeopleItemResponse getItem(String itemName) {
        NeopleItemResponse response = webClient.get().uri(uriBuilder -> uriBuilder
                        .path("/df/items")
                        .queryParam("itemName", itemName)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve().bodyToMono(NeopleItemResponse.class)
                .blockOptional()
                .orElseThrow(() ->
                        new ExternalApiException("네오플 API 응답이 비어 있습니다."));

        if(response.getRows() == null) {
            throw new ExternalApiException("네오플 API 응답 내부 rows가 비어 있습니다.");
        }

        return response;
    }

    public NeopleItemExplainResponse getItemExplain(String itemId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/df/items/{itemId}")
                        .queryParam("apikey", apiKey)
                        .build(itemId))
                .retrieve()
                .bodyToMono(NeopleItemExplainResponse.class)
                .blockOptional()
                .orElseThrow(() ->
                        new ExternalApiException("네오플 API 응답이 비어 있습니다."));
    }

    public List<NeopleItemAuctionDto> getItemAuctionPrice(String itemId) {
        NeopleItemAuctionResponse response = webClient.get().uri(uriBuilder -> uriBuilder
                .path("/df/auction")
                .queryParam("itemId", itemId)
                .queryParam("limit", 400)
                .queryParam("sort", "unitPrice:asc")
                .queryParam("apikey", apiKey)
                .build())
                .retrieve().bodyToMono(NeopleItemAuctionResponse.class)
                .blockOptional()
                .orElseThrow(() ->
                        new ExternalApiException("네오플 API 응답이 비어 있습니다."));

        if(response.getRows() == null) {
            throw new ExternalApiException("네오플 API 응답 내부 rows가 비어 있습니다.");
        }

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
                .blockOptional()
                .orElseThrow(() ->
                        new ExternalApiException("네오플 API 응답이 비어 있습니다."));

        if(response.getRows() == null) {
            throw new ExternalApiException("네오플 API 응답 내부 rows가 비어 있습니다.");
        }

        return response.getRows();
    }

}
