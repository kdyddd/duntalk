package com.duntalk.domain.community.controller;

import com.duntalk.domain.community.dto.CommunityCommentRequest;
import com.duntalk.domain.community.dto.CommunityCommentResponse;
import com.duntalk.domain.community.service.CommunityCommentService;
import com.duntalk.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommunityCommentController {

    private final CommunityCommentService communityCommentService;

    @GetMapping("/community/posts/{communityPostId}/comments")
    public List<CommunityCommentResponse> getCommunityComments(
            @PathVariable("communityPostId") Long communityPostId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        Integer memberId = memberPrincipal == null
                ? null
                : memberPrincipal.memberId();

        return communityCommentService.getCommunityComments(communityPostId, memberId);
    }

    @PostMapping("/community/posts/{communityPostId}/comments")
    public Long createCommunityComment(
            @PathVariable("communityPostId") Long communityPostId,
            @RequestBody CommunityCommentRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
            ) {
        Integer memberId = memberPrincipal == null
                ? null
                : memberPrincipal.memberId();

        return communityCommentService.createCommunityComment(communityPostId, request, memberId);

    }

    @DeleteMapping("/community/comments/{communityCommentId}")
    public void deleteCommunityComment(
            @PathVariable("communityCommentId") Long communityCommentId,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        Integer memberId = memberPrincipal == null
                ? null
                : memberPrincipal.memberId();

        communityCommentService.deleteCommunityComment(communityCommentId, memberId);

    }


}
