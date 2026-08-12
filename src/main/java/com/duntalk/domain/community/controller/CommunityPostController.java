package com.duntalk.domain.community.controller;

import com.duntalk.domain.community.dto.CommunityPostListResponse;
import com.duntalk.domain.community.dto.CommunityPostRequest;
import com.duntalk.domain.community.dto.CommunityPostResponse;
import com.duntalk.domain.community.service.CommunityPostService;
import com.duntalk.domain.community.type.CommunityPostSort;
import com.duntalk.domain.community.type.CommunityType;
import com.duntalk.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class CommunityPostController {

    private final CommunityPostService communityPostService;

    @GetMapping("/community/posts")
    public Page<CommunityPostListResponse> getCommunityPostList(
            @RequestParam(required = false) CommunityType type,
            @RequestParam(defaultValue = "LATEST") CommunityPostSort postSort,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return communityPostService.getCommunityPostList(type, postSort, keyword, pageable);

    }

    @GetMapping("/community/posts/{communityPostId}")
    public CommunityPostResponse getCommunityPost(
            @PathVariable("communityPostId") Long communityPostId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        Integer memberId = memberPrincipal == null
                ? null
                : memberPrincipal.memberId();

        return communityPostService.getCommunityPost(communityPostId, memberId);
    }

    @PostMapping("/community/posts")
    public Long createCommunityPost(
            @RequestBody CommunityPostRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ){
        return communityPostService.createCommunityPost(request, memberPrincipal.memberId());
    }

    @PutMapping("/community/posts/{communityPostId}")
    public void updateCommunityPost(
            @RequestBody CommunityPostRequest request,
            @PathVariable("communityPostId") Long communityPostId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ){
        Integer memberId = memberPrincipal == null
                ? null
                : memberPrincipal.memberId();
        communityPostService.updateCommunityPost(request, communityPostId, memberId);
    }

    @DeleteMapping("/community/posts/{communityPostId}")
    public void deleteCommunityPost(
            @PathVariable("communityPostId") Long communityPostId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ){
        Integer memberId = memberPrincipal == null
                ? null
                : memberPrincipal.memberId();
        communityPostService.deleteCommunityPost(communityPostId, memberId);
    }

}
