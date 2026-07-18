package com.duntalk.domain.member.controller;

import com.duntalk.domain.member.dto.PendingSignup;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.service.MemberService;
import com.duntalk.domain.member.type.SocialProvider;
import com.duntalk.global.security.MemberPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        PendingSignup pendingSignup =
                (PendingSignup) session.getAttribute(
                        PendingSignup.SESSION_KEY
                );

        if (pendingSignup == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "잘못된 회원가입 접근입니다."
            );
        }

        Member member =
                memberService.signup(pendingSignup);

        memberService.login(
                member,
                request,
                response
        );

        session.removeAttribute(PendingSignup.SESSION_KEY);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/signup-status")
    public ResponseEntity<Boolean> getSignupStatus(
            HttpSession session
    ) {
        boolean signupAllowed =
                session.getAttribute(
                        PendingSignup.SESSION_KEY
                ) != null;

        return ResponseEntity.ok(signupAllowed);
    }


}
