package com.duntalk.domain.megaphone.controller;

import com.duntalk.domain.megaphone.dto.MegaphoneRequest;
import com.duntalk.domain.megaphone.dto.MegaphoneResponse;
import com.duntalk.domain.megaphone.service.MegaphoneService;
import com.duntalk.global.security.MemberPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MegaphoneController {
    private final MegaphoneService megaphoneService;

    @PostMapping("/megaphones")
    public boolean createMegaphone(
            @Valid @RequestBody MegaphoneRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        return megaphoneService.createMegaphone(request.content(), memberPrincipal.memberId());
    }

    @GetMapping("/megaphones")
    public List<MegaphoneResponse> getMegaphones() {
        return megaphoneService.getMegaphones();
    }

}
