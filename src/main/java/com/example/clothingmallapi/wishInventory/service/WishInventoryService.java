package com.example.clothingmallapi.wishInventory.service;

import com.example.clothingmallapi.common.StatusMessageResponseDto;
import com.example.clothingmallapi.item.entity.Item;
import com.example.clothingmallapi.item.repository.ItemRepository;
import com.example.clothingmallapi.security.UserDetailsImpl;
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

        Users user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("사용자를 찾을 수 없습니다.")
        );
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

    public StatusMessageResponseDto deleteWishInventory(Long userId, Long wishInventoryId){
        WishInventory wishInventory = wishInventoryRepository.findById(wishInventoryId).orElseThrow(
                () -> new IllegalArgumentException("찜 서랍을 찾을 수 없습니다.")
        );
        if(wishInventory.getUser().getId().equals(userId)){
            wishInventoryRepository.deleteById(wishInventoryId);
            return new StatusMessageResponseDto("찜 서랍이 삭제되었습니다.");
        }
        throw new IllegalArgumentException("내 찜 서랍이 아닙니다.");
    }

    @Transactional
    public StatusMessageResponseDto pickupItemToWishInventory(Long userId, Long wishInventoryId, Long itemId){
        if(wishInventoryRepository.existsWishInventoryByUserIdAndItemId(userId, itemId)){
            throw new IllegalArgumentException("내 다른 찜 서랍에 있는 상품입니다.");
        }
        WishInventory wishInventory = wishInventoryRepository.findById(wishInventoryId).orElseThrow(
                () -> new IllegalArgumentException("찜 서랍을 찾을 수 없습니다.")
        );
        if(wishInventory.getUser().getId().equals(userId)){
            Item item = itemRepository.findById(itemId).orElseThrow();
            wishInventory.addItemToItemList(item);
            return new StatusMessageResponseDto("찜 서랍에 찜이 추가되었습니다.");
        }
        throw new IllegalArgumentException("내 찜 서랍이 아닙니다.");
    }

    @Transactional
    public StatusMessageResponseDto pickoutItemFromWishInventory(Long userId, Long wishInventoryId, Long itemId){
        WishInventory wishInventory = wishInventoryRepository.findById(wishInventoryId).orElseThrow(
                () -> new IllegalArgumentException("찜 서랍을 찾을 수 없습니다.")
        );
        if(wishInventory.getUser().getId().equals(userId)){
            Item pickedItem = itemRepository.findById(itemId).orElseThrow();
            List<Item> itemList = wishInventory.getItems();
            if(itemList.contains(pickedItem)){
                wishInventory.removeItemFromItemList(pickedItem);
            }
            return new StatusMessageResponseDto("찜 서랍에서 찜이 해제됐습니다.");
        }
        throw new IllegalArgumentException("내 찜 서랍이 아닙니다.");
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
