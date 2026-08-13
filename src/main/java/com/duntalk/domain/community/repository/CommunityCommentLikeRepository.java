package com.duntalk.domain.community.repository;

import com.duntalk.domain.community.entity.CommunityCommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityCommentLikeRepository extends JpaRepository<CommunityCommentLike, Long> {
    Optional<CommunityCommentLike> findByCommentIdAndMemberId(Long communityCommentId, Integer memberId);
}
