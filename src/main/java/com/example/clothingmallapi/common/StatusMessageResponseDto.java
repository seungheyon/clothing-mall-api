package com.example.clothingmallapi.common;

import lombok.Getter;

@Getter
public class StatusMessageResponseDto implements GeneralResponseDto {
    private final String message;

    public StatusMessageResponseDto(String message) {
        this.message = message;
    }
}
