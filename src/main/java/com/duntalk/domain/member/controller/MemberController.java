package com.duntalk.domain.member.controller;

import com.duntalk.domain.member.dto.CharacterEquipmentResponse;
import com.duntalk.domain.member.dto.CharacterResponse;
import com.duntalk.domain.member.dto.PendingSignup;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.service.MemberService;
import com.duntalk.domain.member.type.ServerId;
import com.duntalk.global.exception.BadRequestException;
import com.duntalk.global.security.MemberPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        PendingSignup pendingSignup = (PendingSignup) session.getAttribute(PendingSignup.SESSION_KEY);

        if (pendingSignup == null) {
            throw new BadRequestException("잘못된 회원가입 접근입니다.");
        }

        Member member = memberService.signup(pendingSignup);

        memberService.login(member, request, response);

        session.removeAttribute(PendingSignup.SESSION_KEY);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @GetMapping("/signup-status")
    public boolean getSignupStatus(HttpSession session) {
        return session.getAttribute(PendingSignup.SESSION_KEY) != null;
    }

    @GetMapping("/character")
    public CharacterResponse searchCharacter(@RequestParam ServerId serverId, @RequestParam String characterName) {
        return memberService.searchCharacter(serverId, characterName);
    }

    @PostMapping("/character/equipment")
    public CharacterEquipmentResponse startCharacterVerification(@AuthenticationPrincipal MemberPrincipal memberPrincipal,
                                                      @RequestParam ServerId serverId,@RequestParam String characterId, HttpSession session) {
        return memberService.getCharacterEquipment(memberPrincipal.memberId(), serverId, characterId, session);
    }

    @PostMapping("/character/equipment/confirm")
    public boolean checkCharacterVerification(@AuthenticationPrincipal MemberPrincipal memberPrincipal, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        return memberService.checkCharacterEquipment(memberPrincipal.memberId(), session, request, response);
    }


}
