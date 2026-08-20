package com.duntalk.domain.community.service;

import com.duntalk.domain.community.dto.CommunityCommentRequest;
import com.duntalk.domain.community.entity.CommunityComment;
import com.duntalk.domain.community.entity.CommunityPost;
import com.duntalk.domain.community.repository.CommunityCommentRepository;
import com.duntalk.domain.community.repository.CommunityPostRepository;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.global.exception.BadRequestException;
import com.duntalk.global.exception.ForbiddenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CommunityCommentServiceTest {

    @Mock
    CommunityPostRepository communityPostRepository;

    @Mock
    CommunityPost post;

    @Mock
    MemberRepository memberRepository;

    @Mock
    Member writer;

    @Mock
    CommunityCommentRepository communityCommentRepository;

    @Mock
    CommunityComment comment;

    @InjectMocks
    private CommunityCommentService communityCommentService;

    @Mock
    private CommunityComment rootComment;

    @Test
    void 대댓글에_답글을_달면_예외가_발생한다() {
        given(communityPostRepository.findByIdAndDeletedFalse(1L))
                .willReturn(Optional.of(post));

        given(memberRepository.findById(2))
                .willReturn(Optional.of(writer));

        given(communityCommentRepository.findByIdAndDeletedFalse(1L))
                .willReturn(Optional.of(comment));

        given(comment.getPost())
                .willReturn(post);

        given(post.getId())
                .willReturn(1L);

        given(comment.getParent())
                .willReturn(rootComment);

        CommunityCommentRequest request = new CommunityCommentRequest(
                "내용",
                1L
        );

        assertThrows(BadRequestException.class,
                () -> communityCommentService.createCommunityComment(1L, request, 2));

    }

    @Test
    void 다른_회원의_댓글을_삭제하려_하면_예외가_발생한다() {
        given(communityCommentRepository.findByIdAndDeletedFalse(1L))
                .willReturn(Optional.of(comment));

        given(comment.getWriter())
                .willReturn(writer);

        given(writer.getId())
                .willReturn(1);

        assertThrows(ForbiddenException.class,
                () -> communityCommentService.deleteCommunityComment(1L, 2));
    }
}
