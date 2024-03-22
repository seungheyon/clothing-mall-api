package com.example.clothingmallapi.wishInventory.service;

import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryRequestDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryResponseDto;
import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import com.example.clothingmallapi.wishInventory.repository.WishInventoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishInventoryService {

    private final WishInventoryRepository wishInventoryRepository;
    private final UsersRepository userRepository;


    public WishInventoryService(WishInventoryRepository wishInventoryRepository, UsersRepository userRepository) {
        this.wishInventoryRepository = wishInventoryRepository;
        this.userRepository = userRepository;
    }

    public WishInventory createWishInventory(Long userId, WishInventoryRequestDto wishInventoryRequestDto){

        Users user = userRepository.findById(userId).orElseThrow();
        return wishInventoryRepository.save(WishInventory
                .builder()
                .user(user)
                .name(wishInventoryRequestDto.getName())
                .build());
    }

    public void getWishInventories(Long userId){
        List<WishInventoryResponseDto> wishInventoryResponseDtoList = new ArrayList<>();
        List<WishInventory> wishInventoryList = wishInventoryRepository.findAllByUserId(userId);
    }

    public void deleteWishInventory(Long wishInventoryId){
        wishInventoryRepository.deleteById(wishInventoryId);
    }

}
