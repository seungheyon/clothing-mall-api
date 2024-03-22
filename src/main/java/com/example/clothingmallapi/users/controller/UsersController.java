package com.example.clothingmallapi.users.controller;

import com.example.clothingmallapi.users.dto.UsersRequestdto;
import com.example.clothingmallapi.users.service.UsersService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/")
    public void userCreate(@RequestBody UsersRequestdto usersRequestdto){
        usersService.createUser(usersRequestdto);
    }
}
