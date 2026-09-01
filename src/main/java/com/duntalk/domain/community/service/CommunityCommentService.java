package com.duntalk.domain.community.service;

import com.duntalk.domain.community.dto.CommunityCommentDto;
import com.duntalk.domain.community.dto.CommunityCommentRequest;
import com.duntalk.domain.community.dto.CommunityCommentResponse;
import com.duntalk.domain.community.entity.CommunityComment;
import com.duntalk.domain.community.entity.CommunityCommentLike;
import com.duntalk.domain.community.entity.CommunityPost;
import com.duntalk.domain.community.repository.CommunityCommentLikeRepository;
import com.duntalk.domain.community.repository.CommunityCommentRepository;
import com.duntalk.domain.community.repository.CommunityPostRepository;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.global.exception.BadRequestException;
import com.duntalk.global.exception.ForbiddenException;
import com.duntalk.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunityCommentService {

    private final CommunityPostRepository communityPostRepository;
    private final CommunityCommentRepository communityCommentRepository;
    private final MemberRepository memberRepository;
    private final CommunityCommentLikeRepository communityCommentLikeRepository;

    @Transactional
    public Long createCommunityComment(Long communityPostId, CommunityCommentRequest request, Integer memberId) {
        CommunityPost post = communityPostRepository.findByIdAndDeletedFalse(communityPostId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("게시글을 찾을 수 없습니다.")
                );

        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("회원을 찾을 수 없습니다.")
                );

        CommunityComment parent = request.parentCommentId() == null
                ? null
                : communityCommentRepository.findByIdAndDeletedFalse(request.parentCommentId())
                  .orElseThrow(() ->
                        new ResourceNotFoundException("댓글을 찾을 수 없습니다.")
                  );

        if (parent != null && !Objects.equals(parent.getPost().getId(), communityPostId)) {
            throw new BadRequestException("다른 게시글의 댓글에는 답글을 작성할 수 없습니다.");
        }

        if (parent != null && parent.getParent() != null) {
            throw new BadRequestException("대댓글에는 답글을 작성할 수 없습니다.");
        }

        CommunityComment comment = CommunityComment.create(parent, post, writer, request.content());
        CommunityComment savedComment = communityCommentRepository.save(comment);
        communityPostRepository.increaseCommentCount(communityPostId);
        return savedComment.getId();
    }

    public List<CommunityCommentResponse> getCommunityComments(Long communityPostId, Integer memberId) {
        communityPostRepository.findByIdAndDeletedFalse(communityPostId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("게시글을 찾을 수 없습니다.")
                );

        List<CommunityCommentDto> commentDtos = communityCommentRepository.findCommentList(communityPostId, memberId);

        return commentDtos.stream().map(communityCommentDto -> {

            if(communityCommentDto.deleted()) {
                return CommunityCommentResponse.deleted(communityCommentDto);
            }

            String writerName = communityCommentDto.adventureName() == null
                    ? createTemporaryNickname(communityCommentDto.writerId())
                    : communityCommentDto.adventureName();

            boolean isWriter = Objects.equals(communityCommentDto.writerId(), memberId);
            return CommunityCommentResponse.from(communityCommentDto, writerName, isWriter);

        }).toList();

    }

    private String createTemporaryNickname(Integer memberId) {
        long mixedNumber = Math.floorMod(memberId * 7_919L + 284_731L, 1_000_000L);

        return "임시회원#" + String.format("%06d", mixedNumber);
    }

    @Transactional
    public void deleteCommunityComment(Long communityCommentId, Integer memberId) {
        CommunityComment comment = communityCommentRepository.findByIdAndDeletedFalse(communityCommentId)
                .orElseThrow(() ->
                    new ResourceNotFoundException("댓글을 찾을 수 없습니다.")
                );

        if(!Objects.equals(comment.getWriter().getId(), memberId)) {
            throw new ForbiddenException("댓글을 삭제할 권한이 없습니다.");
        }

        comment.delete();
        communityPostRepository.decreaseCommentCount(comment.getPost().getId());
    }

    @Transactional
    public void changeLikedComment(Long communityCommentId, Integer memberId) {
        Optional<CommunityCommentLike> commentLike = communityCommentLikeRepository.findByCommentIdAndMemberId(communityCommentId, memberId);
        CommunityComment comment = communityCommentRepository.findByIdAndDeletedFalse(communityCommentId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("댓글을 찾을 수 없습니다."));
        if (commentLike.isPresent()) {
            communityCommentLikeRepository.delete(commentLike.get());
            comment.decreaseLiked();
        }else {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("회원을 찾을 수 없습니다."));
            communityCommentLikeRepository.save(CommunityCommentLike.create(comment, member));
            comment.increaseLiked();
        }

    }
}
