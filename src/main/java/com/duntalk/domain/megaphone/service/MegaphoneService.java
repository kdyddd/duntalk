package com.duntalk.domain.megaphone.service;

import com.duntalk.domain.megaphone.dto.MegaphoneResponse;
import com.duntalk.domain.megaphone.entity.Megaphone;
import com.duntalk.domain.megaphone.repository.MegaphoneRepository;
import com.duntalk.domain.megaphone.type.ModerationResult;
import com.duntalk.domain.member.entity.DnfCharacter;
import com.duntalk.domain.member.entity.Member;
import com.duntalk.domain.member.repository.DnfCharacterRepository;
import com.duntalk.domain.member.repository.MemberRepository;
import com.duntalk.global.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MegaphoneService {

    private final ModerationService moderationService;
    private final MegaphoneRepository megaphoneRepository;
    private final MemberRepository memberRepository;
    private final DnfCharacterRepository dnfCharacterRepository;

    private final StringRedisTemplate stringRedisTemplate;
    private final JsonMapper jsonMapper;

    public boolean createMegaphone(String content, Integer memberId) {
        Member writer = memberRepository.findById(memberId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("회원을 찾을 수 없습니다.")
                );

        DnfCharacter character = dnfCharacterRepository.findByMember_Id(memberId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("모험단 정보를 찾을 수 없습니다.")
                );

        ModerationResult result = moderationService.moderate(content);

        Megaphone megaphone = megaphoneRepository.save(Megaphone.from(writer, content, result));

        if (!result.approved()) {
            return false;
        }

        MegaphoneResponse response = MegaphoneResponse.from(megaphone, character.getAdventureName());

        String value = jsonMapper.writeValueAsString(response);

        long expireAt = Instant.now()
                .plus(Duration.ofHours(24))
                .getEpochSecond();

        stringRedisTemplate.opsForZSet()
                .add("megaphone:list", value, expireAt);

        return true;
    }

    public List<MegaphoneResponse> getMegaphones() {
        long now = Instant.now().getEpochSecond();

        stringRedisTemplate.opsForZSet()
                .removeRangeByScore("megaphone:list", 0, now);

        Set<String> values = stringRedisTemplate.opsForZSet()
                .reverseRangeByScore("megaphone:list", now, Double.POSITIVE_INFINITY);

        if (values == null || values.isEmpty()) {
            return List.of();
        }

        return values.stream()
                .map(value ->
                        jsonMapper.readValue(value, MegaphoneResponse.class)
                )
                .toList();
    }
}
