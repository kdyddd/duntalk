package com.duntalk.global.security;

public record CsrfTokenResponse(
        String headerName,
        String token
) {
}
