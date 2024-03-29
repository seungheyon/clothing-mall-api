package com.example.clothingmallapi.wishInventory.service;

import com.example.clothingmallapi.item.entity.Item;
import com.example.clothingmallapi.item.repository.ItemRepository;
import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryDetailDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoriesResponseDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryRequestDto;
import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import com.example.clothingmallapi.wishInventory.repository.WishInventoryRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public WishInventory createWishInventory(Long userId, WishInventoryRequestDto wishInventoryRequestDto){

        Users user = userRepository.findById(userId).orElseThrow();
        if(wishInventoryRepository.existsByUserIdAndName(userId, wishInventoryRequestDto.getName())){
            throw new IllegalArgumentException("같은 이름의 찜 서랍이 이미 존재합니다.");
        }
        return wishInventoryRepository.save(WishInventory
                .builder()
                .user(user)
                .name(wishInventoryRequestDto.getName())
                .build());
    }

    public WishInventoriesResponseDto getWishInventories(Long userId, Pageable pageable, Long cursor){
        return new WishInventoriesResponseDto(
                wishInventoryRepository.findAllByUserIdAndIdGreaterThanOrderById(userId,cursor, pageable),
                cursor + pageable.getPageSize());
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

    public WishInventoryDetailDto getItemsInWishInventory(Long wishInventoryId, Pageable pageable, Long cursor){
        return new WishInventoryDetailDto(
                wishInventoryRepository.findItemsByWishInventoryIdAndCursor(wishInventoryId, cursor, pageable),
                cursor + pageable.getPageSize());
    }

    public WishInventory getWishInventory(Long wishInventoryId){
        return wishInventoryRepository.findById(wishInventoryId).orElseThrow();
    }
}
