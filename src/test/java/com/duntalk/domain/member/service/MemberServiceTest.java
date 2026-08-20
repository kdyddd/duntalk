package com.duntalk.domain.member.service;

import com.duntalk.domain.member.dto.*;
import com.duntalk.domain.member.entity.DnfCharacter;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.DnfCharacterRepository;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.domain.member.type.MemberRole;
import com.duntalk.domain.member.type.ServerId;
import com.duntalk.domain.member.type.SocialProvider;
import com.duntalk.global.exception.ConflictException;
import com.duntalk.global.exception.ForbiddenException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    SecurityContextRepository securityContextRepository;

    @Mock
    NeopleMemberApiService neopleMemberApiService;

    @Mock
    DnfCharacterRepository dnfCharacterRepository;

    @Mock
    HttpSession session;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpServletResponse response;

    @InjectMocks
    MemberService memberService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void 이미_가입된_소셜_계정으로_가입하면_예외가_발생한다() {
        given(memberRepository.existsByProviderAndProviderId(SocialProvider.GOOGLE, "123"))
                .willReturn(true);

        PendingSignup pendingSignup = new PendingSignup(
                SocialProvider.GOOGLE,
                "123",
                "test@test.com"
        );

        assertThrows(ConflictException.class,
                () -> memberService.signup(pendingSignup));
    }

    @Test
    void 인증_장비가_해제되면_캐릭터_인증이_완료된다() {
        PendingCharacterVerification pending =
                new PendingCharacterVerification(
                        1,
                        ServerId.CAIN,
                        "character1",
                        "WEAPON",
                        LocalDateTime.now().plusMinutes(5)
                );

        given(session.getAttribute("PENDING_CHARACTER_VERIFICATION"))
                .willReturn(pending);

        NeopleEquipmentDto equipment =
                new NeopleEquipmentDto(
                        "JACKET",
                        "상의",
                        "item1",
                        "테스트 아이템"
                );

        NeopleEquipmentResponse equipmentResponse =
                new NeopleEquipmentResponse(
                        "테스트캐릭터",
                        "테스트모험단",
                        List.of(equipment)
                );

        given(neopleMemberApiService.getCharacterEquipmentAll(
                ServerId.CAIN,
                "character1"
        )).willReturn(equipmentResponse);

        Member member = Member.create(SocialProvider.GOOGLE, "123", "test@test.com");

        ReflectionTestUtils.setField(member, "id", 1);

        given(memberRepository.findById(1))
                .willReturn(Optional.of(member));

        given(dnfCharacterRepository.existsById("character1"))
                .willReturn(false);

        boolean result = memberService.checkCharacterEquipment(
                1,
                session,
                request,
                response
        );

        assertTrue(result);

        assertEquals(MemberRole.ADVENTURE, member.getRole());

        verify(dnfCharacterRepository)
                .save(any(DnfCharacter.class));

        verify(session)
                .removeAttribute("PENDING_CHARACTER_VERIFICATION");

        verify(memberRepository)
                .save(member);
    }

    @Test
    void 이미_등록된_캐릭터를_인증하면_예외가_발생한다() {
        PendingCharacterVerification pending =
                new PendingCharacterVerification(
                        1,
                        ServerId.CAIN,
                        "character1",
                        "WEAPON",
                        LocalDateTime.now().plusMinutes(5)
                );

        given(session.getAttribute("PENDING_CHARACTER_VERIFICATION"))
                .willReturn(pending);

        NeopleEquipmentDto equipment =
                new NeopleEquipmentDto(
                        "JACKET",
                        "상의",
                        "item1",
                        "테스트 아이템"
                );

        NeopleEquipmentResponse equipmentResponse =
                new NeopleEquipmentResponse(
                        "테스트캐릭터",
                        "테스트모험단",
                        List.of(equipment)
                );

        given(neopleMemberApiService.getCharacterEquipmentAll(
                ServerId.CAIN,
                "character1"
        )).willReturn(equipmentResponse);

        Member member = Member.create(SocialProvider.GOOGLE, "123", "test@test.com");

        given(memberRepository.findById(1))
                .willReturn(Optional.of(member));

        given(dnfCharacterRepository.existsById("character1"))
                .willReturn(true);

        assertThrows(
                ConflictException.class,
                () -> memberService.checkCharacterEquipment(1, session, request, response)
        );
    }
}
