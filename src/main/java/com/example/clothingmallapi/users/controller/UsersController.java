package com.example.clothingmallapi.users.controller;

import com.example.clothingmallapi.users.dto.LoginRequestDto;
import com.example.clothingmallapi.users.dto.LoginResponseDto;
import com.example.clothingmallapi.users.dto.SignupRequestDto;
import com.example.clothingmallapi.users.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class UsersController {
    private final UsersService usersService;

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PostMapping("/users/signup")
    public void signup(@RequestBody SignupRequestDto signupRequestDto){
        try{
            usersService.signup(signupRequestDto);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/users/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        return usersService.login(loginRequestDto, response);
    }

}
