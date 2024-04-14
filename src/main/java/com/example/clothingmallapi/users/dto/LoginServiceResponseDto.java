package com.example.clothingmallapi.users.dto;

import com.example.clothingmallapi.common.GeneralResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginServiceResponseDto implements GeneralResponseDto {
    private Long userId;
    private String emailId;
}
