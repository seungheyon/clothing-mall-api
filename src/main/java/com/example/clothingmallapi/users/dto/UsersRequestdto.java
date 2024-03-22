package com.example.clothingmallapi.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsersRequestdto {

    private String emailId;

    private String password;

    private String name;
}
