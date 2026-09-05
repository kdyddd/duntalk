package com.duntalk.domain.item.dto;

public record NeopleApiErrorResponse(
        ErrorDetail error
) {
    public record ErrorDetail(
            int status,
            String code,
            String message
    ) {

    }
}
