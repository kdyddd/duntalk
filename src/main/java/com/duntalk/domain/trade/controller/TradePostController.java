package com.duntalk.domain.trade.controller;

import com.duntalk.domain.trade.dto.TradePostRequest;
import com.duntalk.domain.trade.dto.TradePostListResponse;
import com.duntalk.domain.trade.dto.TradePostResponse;
import com.duntalk.domain.trade.service.TradePostService;
import com.duntalk.domain.trade.type.TradePostSort;
import com.duntalk.domain.trade.type.TradePostStatus;
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

    @PostMapping("/tradePosts")
    public Long createTradePost(
            @Valid @RequestBody TradePostRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return tradePostService.createTradePost(request, memberPrincipal.memberId());
    }

    @GetMapping("/tradePosts")
    public Page<TradePostListResponse> getTradePostList(
            @RequestParam(required = false) TradePostType type,
            @RequestParam(defaultValue = "LATEST") TradePostSort postSort,
            @RequestParam(required = false) TradePostStatus status,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return tradePostService.getTradePostList(type, postSort, status, keyword, pageable);
    }

    @GetMapping("/tradePosts/{tradePostId}")
    public TradePostResponse getTradePost(@PathVariable Long tradePostId) {
        return tradePostService.getTradePost(tradePostId);
    }

    @PutMapping("/tradePosts/{tradePostId}")
    public void changeTradePostStatus(
            @PathVariable Long tradePostId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        tradePostService.changeTradePostStatus(tradePostId, memberPrincipal.memberId());
    }

    @DeleteMapping("/tradePosts/{tradePostId}")
    public void deleteTradePost(
            @PathVariable Long tradePostId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        tradePostService.deleteTradePost(tradePostId, memberPrincipal.memberId());
    }


}
