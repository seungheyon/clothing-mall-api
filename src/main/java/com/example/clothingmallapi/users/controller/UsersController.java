package com.example.clothingmallapi.users.controller;

import com.example.clothingmallapi.common.GeneralResponseDto;
import com.example.clothingmallapi.jwt.JwtUtil;
import com.example.clothingmallapi.users.dto.LoginControllerResponseDto;
import com.example.clothingmallapi.users.dto.LoginRequestDto;
import com.example.clothingmallapi.users.dto.LoginServiceResponseDto;
import com.example.clothingmallapi.users.dto.SignupRequestDto;
import com.example.clothingmallapi.common.StatusMessageResponseDto;
import com.example.clothingmallapi.users.service.TokenGenerator;
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
    private final TokenGenerator tokenGenerator;

    public UsersController(UsersService usersService, TokenGenerator tokenGenerator) {
        this.usersService = usersService;
        this.tokenGenerator = tokenGenerator;
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
            LoginServiceResponseDto loginServiceResponseDto = usersService.login(loginRequestDto, response);
            response.addHeader(JwtUtil.AUTHORIZATION_HEADER, tokenGenerator.createToken(loginServiceResponseDto.getEmailId()));
            return new LoginControllerResponseDto(loginServiceResponseDto.getUserId());
        }
        catch (Exception e){
            return new StatusMessageResponseDto(e.getMessage());
        }
    }

}
