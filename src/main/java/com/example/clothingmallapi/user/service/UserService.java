package com.example.clothingmallapi.user.service;

import com.example.clothingmallapi.user.dto.UserRequestdto;
import com.example.clothingmallapi.user.entity.User;
import com.example.clothingmallapi.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(UserRequestdto userRequestdto){
        userRepository.save(User.builder()
                .emailId(userRequestdto.getEmailId())
                .password(userRequestdto.getPassword())
                .name(userRequestdto.getName())
                .build());
    }
}
