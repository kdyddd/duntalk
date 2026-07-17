package com.duntalk.global.config.security.oauth;

import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.domain.member.type.SocialProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {
    private final MemberRepository memberRepository;
    private final OidcUserService oidcUserService = new OidcUserService();

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        Optional<Member> member = memberRepository.findByProviderAndProviderId(SocialProvider.GOOGLE, oidcUser.getSubject());
        if(member.isEmpty()) {
            memberRepository.save(Member.create(SocialProvider.GOOGLE, oidcUser.getSubject(), oidcUser.getEmail()));
        }
        return oidcUser;
    }
}
