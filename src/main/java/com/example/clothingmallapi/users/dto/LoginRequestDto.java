package com.example.clothingmallapi.users.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    String emailId;

    String password;
}
