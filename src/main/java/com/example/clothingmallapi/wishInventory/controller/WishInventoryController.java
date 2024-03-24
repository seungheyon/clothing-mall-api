package com.example.clothingmallapi.wishInventory.controller;

import com.example.clothingmallapi.wishInventory.dto.WishInventoryRequestDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryResponseDto;
import com.example.clothingmallapi.wishInventory.service.WishInventoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WishInventoryController {

    private final WishInventoryService wishInventoryService;

    public WishInventoryController(WishInventoryService wishInventoryService) {
        this.wishInventoryService = wishInventoryService;
    }

    @PostMapping("/wishInventories")
    public void createWishInventory(@RequestParam Long userId, @RequestBody WishInventoryRequestDto wishInventoryRequestDto){
        wishInventoryService.createWishInventory(userId, wishInventoryRequestDto);
    }

    @GetMapping("/wishInventories")
    public List<WishInventoryResponseDto> getWishInventories(@RequestParam Long userId,
                                                             @RequestParam(name = "cursor", required = false) Long cursor,
                                                             @RequestParam(name = "pageSize", defaultValue = "5") int pageSize,
                                                             @RequestParam(name = "pageNumber")int pageNumber){

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by("id"));

//        if(cursor != null){
//            pageRequest = pageRequest.next();
//        }

        return wishInventoryService.getWishInventories(userId, pageRequest);
    }

    @DeleteMapping("/wishInventories/{wishInventoryId}")
    public void deleteWishInventory(@PathVariable Long wishInventoryId){
        wishInventoryService.deleteWishInventory(wishInventoryId);
    }

    @PostMapping("/wishInventories/{wishInventoryId}")
    public void pickupItemToWishInventory(@PathVariable Long wishInventoryId,
                                          @RequestParam Long itemId,
                                          @RequestParam Boolean isItemPickedUp){
        if(isItemPickedUp.equals(true)){
            wishInventoryService.pickoutItemFromWishInventory(wishInventoryId, itemId);
            return;
        }
        wishInventoryService.pickupItemToWishInventory(wishInventoryId, itemId);
    }

}
