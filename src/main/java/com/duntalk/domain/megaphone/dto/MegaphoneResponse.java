package com.duntalk.domain.megaphone.dto;

import com.duntalk.domain.megaphone.entity.Megaphone;

public record MegaphoneResponse(
        Long id,
        String writer,
        String content
) {
    public static MegaphoneResponse from(Megaphone megaphone, String writerName) {
        return new MegaphoneResponse(
                megaphone.getId(),
                writerName,
                megaphone.getContent()
        );
    }
}
