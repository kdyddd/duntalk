package com.duntalk.domain.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class NeopleItemResponse {

    private List<NeopleItemDto> rows;
}
