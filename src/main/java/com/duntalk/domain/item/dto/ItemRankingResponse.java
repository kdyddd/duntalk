package com.duntalk.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemRankingResponse {

    private List<RankedItemResponse> risingItems;
    private List<RankedItemResponse> fallingItems;
}
