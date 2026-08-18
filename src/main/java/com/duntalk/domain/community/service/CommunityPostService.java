package com.duntalk.domain.community.service;

import com.duntalk.domain.community.dto.*;
import com.duntalk.domain.community.entity.CommunityPost;
import com.duntalk.domain.community.entity.CommunityPostLike;
import com.duntalk.domain.community.repository.CommunityCommentRepository;
import com.duntalk.domain.community.repository.CommunityPostLikeRepository;
import com.duntalk.domain.community.repository.CommunityPostRepository;
import com.duntalk.domain.community.type.CommunityPostSort;
import com.duntalk.domain.community.type.CommunityType;
import com.duntalk.domain.item.entity.Item;
import com.duntalk.domain.item.repository.ItemRepository;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.global.exception.ForbiddenException;
import com.duntalk.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityPostService {

    private final CommunityPostRepository communityPostRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final CommunityPostLikeRepository communityPostLikeRepository;

    public Page<CommunityPostListResponse> getCommunityPostList(CommunityType type, CommunityPostSort postSort, String keyword, Pageable pageable) {

        if (keyword != null && keyword.isBlank()) {
            keyword = null;
        }

        Sort sort = switch (postSort) {
            case LATEST -> Sort.by(Sort.Order.desc("createdAt"), Sort.Order.desc("id"));

            case MOST_LIKED -> Sort.by(Sort.Order.desc("likeCount"), Sort.Order.desc("id"));

            case MOST_VIEWED -> Sort.by(Sort.Order.desc("viewCount"), Sort.Order.desc("id"));
        };

        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<CommunityPostListDto> postListDtos = communityPostRepository.findPostList(type, keyword, sortedPageable);



        return postListDtos.map(postListDto -> {
            int commentCount = communityCommentRepository.countByPostIdAndDeletedFalse(postListDto.communityPostId());
            String writerName = postListDto.adventureName() != null ? postListDto.adventureName() : createTemporaryNickname(postListDto.writerId());
            return CommunityPostListResponse.from(postListDto, writerName, commentCount);
        });

    }

    private String createTemporaryNickname(Integer memberId) {
        long mixedNumber = Math.floorMod(memberId * 7_919L + 284_731L, 1_000_000L);

        return "임시회원#" + String.format("%06d", mixedNumber);
    }


    public Long createCommunityPost(CommunityPostRequest request, Integer memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("회원을 찾을 수 없습니다.")
                );

        Item item = null;

        if (request.itemId() != null) {
            item = itemRepository.findById(request.itemId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("아이템을 찾을 수 없습니다.")
                    );
        }

        CommunityPost post = CommunityPost.create(
                member,
                request.type(),
                item,
                request.title(),
                request.content()
        );

        return communityPostRepository.save(post).getId();
    }
    @Transactional
    public CommunityPostResponse getCommunityPost(Long communityPostId, Integer memberId) {
        communityPostRepository.increaseViewCount(communityPostId);

        CommunityPostDto post = communityPostRepository.findPostById(communityPostId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("게시글을 찾을 수 없습니다."));

        String writerName = post.adventureName() != null
                ? post.adventureName()
                : createTemporaryNickname(post.writerId());

        boolean isWriter = Objects.equals(post.writerId(), memberId);

        boolean liked = memberId != null && communityPostLikeRepository.findByPostIdAndMemberId(communityPostId, memberId).isPresent();

        return CommunityPostResponse.from(post, writerName, isWriter, liked);
    }

    public Page<CommunityPostListResponse> getCommunityPostListByItem(String itemId, Pageable pageable) {
        Page<CommunityPostListDto> postListDtos = communityPostRepository.findByItemIdPostList(itemId, pageable);

        return postListDtos.map(postListDto -> {
            int commentCount = communityCommentRepository.countByPostIdAndDeletedFalse(postListDto.communityPostId());
            String writerName = postListDto.adventureName() != null ? postListDto.adventureName() : createTemporaryNickname(postListDto.writerId());
            return CommunityPostListResponse.from(postListDto, writerName, commentCount);
        });
    }

    @Transactional
    public void updateCommunityPost(CommunityPostRequest request, Long communityPostId, Integer memberId) {
        CommunityPost post = communityPostRepository.findByIdAndDeletedFalse(communityPostId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("게시글을 찾을 수 없습니다"));

        if(!Objects.equals(post.getWriter().getId(), memberId)) {
            throw new ForbiddenException("게시글을 수정할 권한이 없습니다.");
        }

        Item item = null;

        if (request.itemId() != null) {
            item = itemRepository.findById(request.itemId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("아이템을 찾을 수 없습니다.")
                    );
        }

        post.update(request.type(), item, request.title(), request.content());
    }

    @Transactional
    public void deleteCommunityPost(Long communityPostId, Integer memberId) {
        CommunityPost post = communityPostRepository.findByIdAndDeletedFalse(communityPostId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("게시글을 찾을 수 없습니다"));

        if(!Objects.equals(post.getWriter().getId(), memberId)) {
            throw new ForbiddenException("게시글을 삭제할 권한이 없습니다.");
        }

        post.delete();
    }

    @Transactional
    public void changeLikedPost(Long communityPostId, Integer memberId) {
        Optional<CommunityPostLike> postLike = communityPostLikeRepository.findByPostIdAndMemberId(communityPostId, memberId);
        CommunityPost post = communityPostRepository.findByIdAndDeletedFalse(communityPostId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("게시글을 찾을 수 없습니다."));
        if (postLike.isPresent()) {
            communityPostLikeRepository.delete(postLike.get());
            post.decreaseLiked();
        }else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("회원을 찾을 수 없습니다."));
            communityPostLikeRepository.save(CommunityPostLike.create(post, member));
            post.increaseLiked();
        }

    }
}
