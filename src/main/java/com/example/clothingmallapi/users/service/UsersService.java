package com.example.clothingmallapi.users.service;

import com.example.clothingmallapi.jwt.JwtUtil;
import com.example.clothingmallapi.users.dto.LoginRequestDto;
import com.example.clothingmallapi.users.dto.LoginServiceResponseDto;
import com.example.clothingmallapi.users.dto.SignupRequestDto;
import com.example.clothingmallapi.common.StatusMessageResponseDto;
import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public StatusMessageResponseDto signup(SignupRequestDto signupRequestDto){

        if(usersRepository.existsByEmailId(signupRequestDto.getEmailId())){
            throw new IllegalArgumentException("이미 사용중인 Email Id 입니다.");
        }

        usersRepository.save(Users.builder()
                .emailId(signupRequestDto.getEmailId())
                .password(signupRequestDto.getPassword())
                .name(signupRequestDto.getName())
                .build());

        return new StatusMessageResponseDto("회원가입이 정상적으로 완료되었습니다.");
    }

    public LoginServiceResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response){

        Users users = usersRepository.findByEmailId(loginRequestDto.getEmailId()).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );

        if(!users.getPassword().equals(loginRequestDto.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return new LoginServiceResponseDto(users.getId(), users.getEmailId());
    }
}
