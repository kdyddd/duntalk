package com.duntalk.domain.community.service;

import com.duntalk.domain.community.dto.CommunityPostRequest;
import com.duntalk.domain.community.entity.CommunityPost;
import com.duntalk.domain.community.entity.CommunityPostLike;
import com.duntalk.domain.community.repository.CommunityPostLikeRepository;
import com.duntalk.domain.community.repository.CommunityPostRepository;
import com.duntalk.domain.community.type.CommunityType;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.global.exception.ForbiddenException;
import com.duntalk.global.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CommunityPostServiceTest {
    @Mock
    private CommunityPostRepository communityPostRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private CommunityPostLikeRepository communityPostLikeRepository;

    @Mock
    private CommunityPost post;

    @Mock
    private Member writer;

    @Mock
    private Member member;

    @Mock
    private CommunityPostLike postLike;

    @InjectMocks
    private CommunityPostService communityPostService;

    @Test
    void 존재하지_않는_게시글을_조회하면_예외가_발생한다() {

        given(communityPostRepository.findPostById(999L))
                .willReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> communityPostService.getCommunityPost(999L, null)
        );

    }

    @Test
    void 다른_회원의_게시글을_수정하려_하면_예외가_발생한다() {

        given(communityPostRepository.findByIdAndDeletedFalse(1L))
                .willReturn(Optional.of(post));

        given(post.getWriter())
                .willReturn(writer);

        given(writer.getId())
                .willReturn(1);

        CommunityPostRequest request = new CommunityPostRequest(
                CommunityType.FREE,
                "제목",
                "내용",
                null
        );

        assertThrows(
                ForbiddenException.class,
                () -> communityPostService.updateCommunityPost(request, 1L, 2)
        );
    }

    @Test
    void 게시글에_좋아요를_누르면_좋아요가_추가된다() {

        given(communityPostLikeRepository.findByPostIdAndMemberId(1L, 2))
                .willReturn(Optional.empty());

        given(communityPostRepository.findByIdAndDeletedFalse(1L))
                .willReturn(Optional.of(post));

        given(memberRepository.findById(2))
                .willReturn(Optional.of(member));

        communityPostService.changeLikedPost(1L, 2);

        verify(communityPostLikeRepository)
                .save(any(CommunityPostLike.class));

        verify(post).increaseLiked();
    }

    @Test
    void 게시글에_좋아요를_다시_누르면_좋아요가_취소된다() {

        given(communityPostLikeRepository.findByPostIdAndMemberId(1L, 2))
                .willReturn(Optional.of(postLike));

        given(communityPostRepository.findByIdAndDeletedFalse(1L))
                .willReturn(Optional.of(post));

        communityPostService.changeLikedPost(1L, 2);

        verify(communityPostLikeRepository)
                .delete(postLike);

        verify(post).decreaseLiked();
    }
}
