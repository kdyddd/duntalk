package com.duntalk.global.security;

import com.duntalk.domain.member.dto.PendingSignup;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.domain.member.service.MemberService;
import com.duntalk.domain.member.type.MemberStatus;
import com.duntalk.domain.member.type.SocialProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2AuthenticationToken oauth =
                (OAuth2AuthenticationToken) authentication;

        OidcUser oidcUser =
                (OidcUser) oauth.getPrincipal();

        SocialProvider provider =
                SocialProvider.from(
                        oauth.getAuthorizedClientRegistrationId()
                );

        Optional<Member> member =
                memberService.findBySocialAccount(
                        provider,
                        oidcUser.getSubject()
                );

        if (member.isPresent()) {
            memberService.login(
                    member.get(),
                    request,
                    response
            );

            response.sendRedirect("/");
            return;
        }

        request.getSession().setAttribute(
                PendingSignup.SESSION_KEY,
                new PendingSignup(
                        provider,
                        oidcUser.getSubject(),
                        oidcUser.getEmail()
                )
        );

        response.sendRedirect("/signup");
    }
}
