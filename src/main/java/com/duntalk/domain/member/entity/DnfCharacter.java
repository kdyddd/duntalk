package com.duntalk.domain.member.entity;

import com.duntalk.domain.member.type.ServerId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DnfCharacter {

    @Id
    private String characterId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "member_id",
            nullable = false,
            unique = true
    )
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServerId serverId;

    @Column(nullable = false)
    private String characterName;

    @Column(nullable = false)
    private String adventureName;

    private DnfCharacter(String characterId, Member member, ServerId serverId, String characterName, String adventureName) {
        this.characterId = characterId;
        this.member = member;
        this.serverId = serverId;
        this.characterName = characterName;
        this.adventureName = adventureName;
    }

    public static DnfCharacter create(String characterId, Member member, ServerId serverId, String characterName, String adventureName) {
        return new DnfCharacter(characterId, member, serverId, characterName, adventureName);
    }

}
