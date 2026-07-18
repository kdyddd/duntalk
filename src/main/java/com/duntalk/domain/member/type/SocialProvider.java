package com.duntalk.domain.member.type;

public enum SocialProvider {
    GOOGLE,
    NAVER;

    public static SocialProvider from(String registrationId) {
        return switch (registrationId) {
            case "google" -> GOOGLE;
            case "naver" -> NAVER;
            default -> throw new IllegalArgumentException(
                    "지원하지 않는 소셜 로그인입니다."
            );
        };
    }
}