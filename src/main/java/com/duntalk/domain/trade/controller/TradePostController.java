package com.duntalk.domain.trade.controller;

import com.duntalk.domain.trade.dto.TradePostRequest;
import com.duntalk.domain.trade.dto.TradePostResponse;
import com.duntalk.domain.trade.service.TradePostService;
import com.duntalk.domain.trade.type.TradePostSort;
import com.duntalk.domain.trade.type.TradePostType;
import com.duntalk.global.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TradePostController {

    private final TradePostService tradePostService;

    @GetMapping("/tradePosts")
    public Page<TradePostResponse> getTradePostList(
            @RequestParam(required = false) TradePostType type,
            @RequestParam(defaultValue = "LATEST") TradePostSort postSort,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return tradePostService.getTradePostList(type, postSort, keyword, pageable);
    }

    @PostMapping("/tradePosts")
    public Long createTradePost(
            @Valid @RequestBody TradePostRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return tradePostService.createTradePost(request, memberPrincipal.memberId());
    }
}
