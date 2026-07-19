package com.duntalk.global.security;

import com.duntalk.domain.member.dto.PendingSignup;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.service.MemberService;
import com.duntalk.domain.member.type.SocialProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuthLoginSuccessHandler
        implements AuthenticationSuccessHandler {

    private final MemberService memberService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2AuthenticationToken oauth =
                (OAuth2AuthenticationToken) authentication;

        OAuth2User oauth2User =
                oauth.getPrincipal();

        SocialProvider provider =
                SocialProvider.from(
                        oauth.getAuthorizedClientRegistrationId()
                );

        String providerId;
        String email;

        if (provider == SocialProvider.GOOGLE) {
            providerId = oauth2User.getAttribute("sub");
            email = oauth2User.getAttribute("email");
        } else {
            Map<String, Object> naverResponse =
                    oauth2User.getAttribute("response");

            providerId = (String) naverResponse.get("id");
            email = (String) naverResponse.get("email");
        }

        Optional<Member> member =
                memberService.findBySocialAccount(
                        provider,
                        providerId
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
                        providerId,
                        email
                )
        );

        response.sendRedirect(
                "/signup?provider=" + provider.name()
        );
    }
}
