package com.duntalk.global.security;

import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.type.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public record MemberPrincipal(
        Integer memberId,
        MemberRole role
) {

    public Collection<? extends GrantedAuthority> authorities() {
        return List.of(
                new SimpleGrantedAuthority(
                        "ROLE_" + role.name()
                )
        );
    }

    public static MemberPrincipal from(Member member) {
        return new MemberPrincipal(
                member.getId(),
                member.getRole()
        );
    }
}