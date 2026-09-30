package com.duntalk.domain.megaphone.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MegaphoneRequest(
        @NotBlank
        @Size(max = 40)
        String content
) {
}
