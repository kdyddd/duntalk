package com.duntalk.domain.member.controller;


import com.duntalk.domain.member.dto.CurrentUserResponse;
import com.duntalk.domain.member.entity.DnfCharacter;
import com.duntalk.domain.member.service.MemberService;
import com.duntalk.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(
            @AuthenticationPrincipal MemberPrincipal principal
    ) {
        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        Integer memberId = principal.memberId();
        Optional<DnfCharacter> dnfCharacter = memberService.findDnfCharacter(memberId);

        CurrentUserResponse response;

        if(dnfCharacter.isPresent()) {
            DnfCharacter character = dnfCharacter.get();

            response = new CurrentUserResponse(memberId, principal.role(), character.getAdventureName(), character.getServerId(), character.getCharacterId(), character.getCharacterName());
        } else {
            response = new CurrentUserResponse(memberId, principal.role(),null, null, null, null);
        }

        return ResponseEntity.ok(response);
    }

}
