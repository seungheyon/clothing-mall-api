package com.example.clothingmallapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequestdto {

    private String emailId;

    private String password;

    private String name;
}
