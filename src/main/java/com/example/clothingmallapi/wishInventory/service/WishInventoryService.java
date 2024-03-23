package com.example.clothingmallapi.wishInventory.service;

import com.example.clothingmallapi.item.entity.Item;
import com.example.clothingmallapi.item.repository.ItemRepository;
import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryRequestDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryResponseDto;
import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import com.example.clothingmallapi.wishInventory.repository.WishInventoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishInventoryService {

    private final WishInventoryRepository wishInventoryRepository;
    private final UsersRepository userRepository;
    private final ItemRepository itemRepository;


    public WishInventoryService(WishInventoryRepository wishInventoryRepository, UsersRepository userRepository, ItemRepository itemRepository) {
        this.wishInventoryRepository = wishInventoryRepository;
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
    }

    public WishInventory createWishInventory(Long userId, WishInventoryRequestDto wishInventoryRequestDto){

        Users user = userRepository.findById(userId).orElseThrow();
        return wishInventoryRepository.save(WishInventory
                .builder()
                .user(user)
                .name(wishInventoryRequestDto.getName())
                .build());
    }

    public List<WishInventoryResponseDto> getWishInventories(Long userId, Pageable pageable){
        List<WishInventoryResponseDto> wishInventoryResponseDtoList = new ArrayList<>();
        List<WishInventory> wishInventoryList = wishInventoryRepository.findAllByUserId(userId, pageable);
        for(WishInventory wishInventory : wishInventoryList){
            wishInventoryResponseDtoList.add(new WishInventoryResponseDto(wishInventory.getId(), wishInventory.getName()));
        }
        return wishInventoryResponseDtoList;
    }

    public void deleteWishInventory(Long wishInventoryId){
        wishInventoryRepository.deleteById(wishInventoryId);
    }

    @Transactional
    public void pickupItemToWishInventory(Long wishInventoryId, Long itemId){
        WishInventory wishInventory = wishInventoryRepository.findById(wishInventoryId).orElseThrow();
        Item item = itemRepository.findById(itemId).orElseThrow();
        wishInventory.addItemToItemList(item);
    }

    @Transactional
    public void pickoutItemFromWishInventory(Long wishInventoryId, Long itemId){
        WishInventory wishInventory = wishInventoryRepository.findById(wishInventoryId).orElseThrow();
        Item pickeditem = itemRepository.findById(itemId).orElseThrow();
        List<Item> itemList = wishInventory.getItems();
        if(itemList.contains(pickeditem)){
            wishInventory.removeItemFromItemList(pickeditem);
        }
    }

}
