package com.example.clothingmallapi.wishInventory.service;

import com.example.clothingmallapi.user.entity.User;
import com.example.clothingmallapi.user.repository.UserRepository;
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
    private final UserRepository userRepository;


    public WishInventoryService(WishInventoryRepository wishInventoryRepository, UserRepository userRepository) {
        this.wishInventoryRepository = wishInventoryRepository;
        this.userRepository = userRepository;
    }

    public void createWishInventory(Long userId, WishInventoryRequestDto wishInventoryRequestDto){

        User user = userRepository.findById(userId).orElseThrow();
        wishInventoryRepository.save(WishInventory
                .builder()
                .user(user)
                .name(wishInventoryRequestDto.getName())
                .build());
    }

//    public List<WishInventoryResponseDto> getWishInventories(Long userId){
//        List<WishInventoryResponseDto> wishInventoryResponseDtoList = new ArrayList<>();
//    }

    public void deleteWishInventory(Long wishInventoryId){
        wishInventoryRepository.deleteById(wishInventoryId);
    }

}
