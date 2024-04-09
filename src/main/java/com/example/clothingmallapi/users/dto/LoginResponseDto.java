package com.example.clothingmallapi.users.dto;

import com.example.clothingmallapi.common.GeneralResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto implements GeneralResponseDto {
    private Long userId;
}
