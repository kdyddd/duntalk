package com.duntalk.domain.member.service;

import com.duntalk.domain.member.dto.PendingSignup;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.domain.member.type.SocialProvider;
import com.duntalk.global.security.MemberPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final SecurityContextRepository securityContextRepository;


    public void login(
            Member member,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        MemberPrincipal principal =
                MemberPrincipal.from(member);

        Authentication authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        principal,
                        null,
                        principal.authorities()
                );

        SecurityContext context =
                SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(
                context,
                request,
                response
        );
    }

    public Member signup(PendingSignup pendingSignup) {

        if (memberRepository.existsByProviderAndProviderId(
                pendingSignup.provider(),
                pendingSignup.providerId()
        )) {
            throw new IllegalStateException(
                    "이미 가입된 회원입니다."
            );
        }

        Member member = Member.create(
                pendingSignup.provider(),
                pendingSignup.providerId(),
                pendingSignup.email()
        );

        return memberRepository.save(member);
    }

    public Optional<Member> findBySocialAccount(
            SocialProvider provider,
            String providerId
    ) {
        return memberRepository.findByProviderAndProviderId(
                provider,
                providerId
        );
    }


}
