package com.duntalk.domain.member.service;

import com.duntalk.domain.member.dto.*;
import com.duntalk.domain.member.entity.DnfCharacter;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.DnfCharacterRepository;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.domain.member.type.ServerId;
import com.duntalk.domain.member.type.SocialProvider;
import com.duntalk.global.security.MemberPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final SecurityContextRepository securityContextRepository;

    private final NeopleMemberApiService neopleMemberApiService;

    private final DnfCharacterRepository dnfCharacterRepository;


    public void login(Member member, HttpServletRequest request, HttpServletResponse response) {
        MemberPrincipal principal = MemberPrincipal.from(member);

        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(principal,null, principal.authorities());

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, request, response);
    }

    public Member signup(PendingSignup pendingSignup) {

        if (memberRepository.existsByProviderAndProviderId(
                pendingSignup.provider(),
                pendingSignup.providerId()
        )) {
            throw new IllegalStateException(
                    "이미 가입된 회원입니다."
            );
        }

        Member member = Member.create(pendingSignup.provider(), pendingSignup.providerId(), pendingSignup.email());

        return memberRepository.save(member);
    }

    public Optional<Member> findBySocialAccount(SocialProvider provider, String providerId){
        return memberRepository.findByProviderAndProviderId(provider, providerId);
    }

    public CharacterResponse searchCharacter(ServerId serverId, String characterName) {
        return CharacterResponse.from(neopleMemberApiService.searchCharacter(serverId, characterName));
    }

    public CharacterEquipmentResponse getCharacterEquipment(Integer memberId, ServerId serverId, String characterId, HttpSession session) {
        List<NeopleEquipmentDto> equipmentList = neopleMemberApiService.getCharacterEquipmentAll(serverId, characterId).equipment();


        if (equipmentList.isEmpty()) {
            throw new IllegalArgumentException("착용한 장비가 없습니다.");
        }

        Random random = new Random();

        int randomIndex = random.nextInt(equipmentList.size());

        NeopleEquipmentDto selectedEquipment = equipmentList.get(randomIndex);

        PendingCharacterVerification pending = new PendingCharacterVerification(memberId, serverId, characterId, selectedEquipment.slotId(), LocalDateTime.now().plusMinutes(5));

        session.setAttribute("PENDING_CHARACTER_VERIFICATION", pending);

        return CharacterEquipmentResponse.from(selectedEquipment);
    }


    public boolean checkCharacterEquipment(Integer memberId, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        PendingCharacterVerification pending = (PendingCharacterVerification) session.getAttribute("PENDING_CHARACTER_VERIFICATION");

        if (pending == null) {
            throw new IllegalStateException("진행 중인 캐릭터 인증이 없습니다.");
        }

        if (!pending.belongsTo(memberId)) {
            session.removeAttribute("PENDING_CHARACTER_VERIFICATION");
            throw new IllegalStateException("현재 회원의 인증 요청이 아닙니다.");
        }

        if (pending.isExpired()) {
            session.removeAttribute("PENDING_CHARACTER_VERIFICATION");
            throw new IllegalStateException("인증 시간이 만료되었습니다.");
        }

        NeopleEquipmentResponse neopleEquipmentResponse = neopleMemberApiService.getCharacterEquipmentAll(pending.serverId(), pending.characterId());

        boolean checkEquipment = true;

        for(NeopleEquipmentDto dto : neopleEquipmentResponse.equipment()) {
            if(dto.slotId().equals(pending.slotId())) {
                checkEquipment = false;
                break;
            }
        }

        if(checkEquipment) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new IllegalStateException("회원 정보를 찾을 수 없습니다."));
            if(dnfCharacterRepository.existsById(pending.characterId())) {
                throw new IllegalStateException("이미 등록된 캐릭터 입니다.");
            }
            member.verifyAdventure();
            dnfCharacterRepository.save(DnfCharacter.create(pending.characterId(), member, pending.serverId(), neopleEquipmentResponse.characterName(), neopleEquipmentResponse.adventureName()));
            session.removeAttribute("PENDING_CHARACTER_VERIFICATION");
            memberRepository.save(member);
            login(member, request, response);
        }

        return checkEquipment;
    }

    public Optional<DnfCharacter> findDnfCharacter(Integer memberId) {

        return dnfCharacterRepository.findByMember_Id(memberId);
    }
}
