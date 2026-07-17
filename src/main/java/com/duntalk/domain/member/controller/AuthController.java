package com.duntalk.domain.member.controller;


import com.duntalk.domain.member.dto.CurrentUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser(@AuthenticationPrincipal OidcUser oidcUser) {
        if(oidcUser == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
        CurrentUserResponse response = new CurrentUserResponse(oidcUser.getSubject(), oidcUser.getEmail(), oidcUser.getFullName());
        return ResponseEntity.ok(response);
    }
}
