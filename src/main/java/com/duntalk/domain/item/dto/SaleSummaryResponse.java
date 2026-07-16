package com.duntalk.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class SaleSummaryResponse {

    private LocalDateTime startTime;
    private Long totalPrice;
    private Long totalCount;
    private Integer avgPrice;
    private Integer minPrice;
    private Integer maxPrice;

}
