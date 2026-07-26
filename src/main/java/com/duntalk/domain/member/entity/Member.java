package com.duntalk.domain.member.entity;

import com.duntalk.domain.member.type.MemberRole;
import com.duntalk.domain.member.type.MemberStatus;
import com.duntalk.domain.member.type.SocialProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(
        name = "members",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_member_provider_provider_id",
                        columnNames = {"provider", "provider_id"}
                )
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialProvider provider;

    @Column(nullable = false)
    private String providerId;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole role;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    private LocalDateTime withdrawnAt;

    private Member(SocialProvider provider, String providerId, String email) {
        this.provider = provider;
        this.providerId = providerId;
        this.email = email;
        this.role = MemberRole.USER;
        this.createdAt = LocalDateTime.now();
        this.status = MemberStatus.ACTIVE;
    }

    public static Member create(SocialProvider provider, String providerId, String email) {
        return new Member(provider, providerId, email);
    }

    public void delete() {
        this.status = MemberStatus.WITHDRAWN;
        this.withdrawnAt = LocalDateTime.now();
    }

    public void verifyAdventure() {
        this.role = MemberRole.ADVENTURE;
    }
}
