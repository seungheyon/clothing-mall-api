package com.example.clothingmallapi.users.controller;

import com.example.clothingmallapi.common.GeneralResponseDto;
import com.example.clothingmallapi.users.dto.LoginRequestDto;
import com.example.clothingmallapi.users.dto.SignupRequestDto;
import com.example.clothingmallapi.common.StatusMessageResponseDto;
import com.example.clothingmallapi.users.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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
    public GeneralResponseDto signup(@RequestBody SignupRequestDto signupRequestDto){
        try{
            return usersService.signup(signupRequestDto);
        }
        catch (Exception e) {
            return new StatusMessageResponseDto(e.getMessage());
        }
    }

    @ResponseBody
    @PostMapping("/users/login")
    public GeneralResponseDto login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response){
        try{
            return usersService.login(loginRequestDto, response);
        }
        catch (Exception e){
            return new StatusMessageResponseDto(e.getMessage());
        }
    }

}
