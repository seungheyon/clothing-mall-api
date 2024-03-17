package com.example.clothingmallapi.user.controller;

import com.example.clothingmallapi.user.dto.UserRequestdto;
import com.example.clothingmallapi.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/")
    public void userCreate(@RequestBody UserRequestdto userRequestdto){
        userService.createUser(userRequestdto);
    }
}
