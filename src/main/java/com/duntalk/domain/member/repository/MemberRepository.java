package com.duntalk.domain.member.repository;

import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.type.SocialProvider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByProviderAndProviderId(
            SocialProvider provider,
            String providerId
    );

    boolean existsByProviderAndProviderId(SocialProvider provider,
                                          String providerId);
}
