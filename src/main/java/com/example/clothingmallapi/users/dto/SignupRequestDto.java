package com.example.clothingmallapi.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupRequestDto {

    private String emailId;

    private String password;

    private String name;
}
