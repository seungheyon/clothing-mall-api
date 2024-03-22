package com.example.clothingmallapi.users.service;

import com.example.clothingmallapi.users.dto.UsersRequestdto;
import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void createUser(UsersRequestdto usersRequestdto){
        usersRepository.save(Users.builder()
                .emailId(usersRequestdto.getEmailId())
                .password(usersRequestdto.getPassword())
                .name(usersRequestdto.getName())
                .build());
    }
}
