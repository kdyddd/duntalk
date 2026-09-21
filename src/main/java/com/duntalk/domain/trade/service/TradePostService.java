package com.duntalk.domain.trade.service;

import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.repository.ItemRepository;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.domain.trade.dto.TradePostListDto;
import com.duntalk.domain.trade.dto.TradePostRequest;
import com.duntalk.domain.trade.dto.TradePostResponse;
import com.duntalk.domain.trade.entity.TradePost;
import com.duntalk.domain.trade.repository.TradePostRepository;
import com.duntalk.domain.trade.type.TradePostSort;
import com.duntalk.domain.trade.type.TradePostType;
import com.duntalk.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TradePostService {

    private final TradePostRepository tradePostRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long createTradePost(TradePostRequest request, Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("회원을 찾을 수 없습니다.")
                );

        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("아이템을 찾을 수 없습니다.")
                );

        TradePost tradePost = TradePost.create(item, request.quantity(), request.price(), member, request.content(), request.type());

        return tradePostRepository.save(tradePost).getId();
    }

    public Page<TradePostResponse> getTradePostList(TradePostType type, TradePostSort postSort, String keyword, Pageable pageable) {
        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        Sort sort = switch (postSort) {
            case LATEST -> Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"));

            case PRICE_DESC -> Sort.by(Sort.Order.desc("price"), Sort.Order.desc("id"));

            case PRICE_ASC -> Sort.by(Sort.Order.asc("price"), Sort.Order.desc("id"));
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<TradePostListDto> postListDtos = tradePostRepository.findPostList(type, keyword, sortedPageable);

        return postListDtos.map(TradePostResponse::from);
    }
}
