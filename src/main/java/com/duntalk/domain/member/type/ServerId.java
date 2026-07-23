package com.duntalk.domain.member.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ServerId {

    ANTON("anton"),
    BAKAL("bakal"),
    CAIN("cain"),
    CASILLAS("casillas"),
    DIREGIE("diregie"),
    HILDER("hilder"),
    PREY("prey"),
    SIROCO("siroco");

    private final String apiValue;
}