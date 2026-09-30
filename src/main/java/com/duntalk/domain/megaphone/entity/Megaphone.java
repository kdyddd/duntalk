package com.duntalk.domain.megaphone.entity;

import com.duntalk.domain.megaphone.type.MegaphoneStatus;
import com.duntalk.domain.megaphone.type.ModerationResult;
import com.duntalk.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Megaphone {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    private String content;

    @Enumerated(EnumType.STRING)
    private MegaphoneStatus status;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private ModerationResult moderationResult;

    private Megaphone(Member writer, String content, ModerationResult moderationResult) {
        this.writer = writer;
        this.content = content;
        this.status = moderationResult.approved()
                ? MegaphoneStatus.APPROVED
                : MegaphoneStatus.BLOCKED;
    }

    public static Megaphone from (Member writer, String content, ModerationResult moderationResult) {
        return new Megaphone(writer, content, moderationResult);
    }
}
