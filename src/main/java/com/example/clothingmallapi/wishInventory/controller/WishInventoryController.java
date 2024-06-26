package com.example.clothingmallapi.wishInventory.controller;

import com.example.clothingmallapi.common.GeneralResponseDto;
import com.example.clothingmallapi.common.StatusMessageResponseDto;
import com.example.clothingmallapi.security.UserDetailsImpl;
import com.example.clothingmallapi.wishInventory.dto.WishInventoriesResponseDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryDetailDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryRequestDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryResponseDto;
import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import com.example.clothingmallapi.wishInventory.service.WishInventoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class WishInventoryController {

    private final WishInventoryService wishInventoryService;

    public WishInventoryController(WishInventoryService wishInventoryService) {
        this.wishInventoryService = wishInventoryService;
    }

    @PostMapping("/wishInventories")
    public GeneralResponseDto createWishInventory(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody WishInventoryRequestDto wishInventoryRequestDto){
        try{
            wishInventoryService.createWishInventory(userDetails.getId(), wishInventoryRequestDto);
        }
        catch (Exception e){
            return new StatusMessageResponseDto(e.getMessage());
        }
        return new StatusMessageResponseDto("찜 서랍이 생성되었습니다.");
    }

    @GetMapping("/wishInventories")
    public WishInventoriesResponseDto getWishInventories(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                         @RequestParam(name = "cursor", required = false) Long cursor,
                                                         @RequestParam(name = "pageSize", defaultValue = "5") int pageSize
                                                         ){
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        return wishInventoryService.getWishInventories(userDetails.getId(), pageRequest, cursor);
    }

    @DeleteMapping("/wishInventories/{wishInventoryId}")
    public GeneralResponseDto deleteWishInventory(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                    @PathVariable Long wishInventoryId){
        try{
            return wishInventoryService.deleteWishInventory(userDetails.getId(), wishInventoryId);
        }
        catch (Exception e){
            return new StatusMessageResponseDto(e.getMessage());
        }
    }

    @PostMapping("/wishInventories/{wishInventoryId}")
    public GeneralResponseDto pickupItemToWishInventory(@PathVariable Long wishInventoryId,
                                          @AuthenticationPrincipal UserDetailsImpl userDetails,
                                          @RequestParam Long itemId,
                                          @RequestParam Boolean isItemPickedUp){
        if(isItemPickedUp.equals(true)){
            try{
                return wishInventoryService.pickoutItemFromWishInventory(userDetails.getId(), wishInventoryId, itemId);
            } catch (Exception e){
                return new StatusMessageResponseDto(e.getMessage());
            }
        }
        try{
            return wishInventoryService.pickupItemToWishInventory(userDetails.getId(), wishInventoryId, itemId);
        } catch (Exception e){
            return new StatusMessageResponseDto(e.getMessage());
        }
    }

//    @GetMapping("/wishInventories/{wishInventoryId}")
//    public WishInventory getWishInventory(@PathVariable Long wishInventoryId){
//        return wishInventoryService.getWishInventory(wishInventoryId);
//    }

    @GetMapping("/wishInventories/{wishInventoryId}")
    public WishInventoryDetailDto getItemsInWishInventory(@PathVariable Long wishInventoryId,
                                                          @RequestParam(name = "cursor", required = false) Long cursor,
                                                          @RequestParam(name = "pageSize", defaultValue = "5") int pageSize
                                                 ){
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        return wishInventoryService.getItemsInWishInventory(wishInventoryId, pageRequest, cursor);
    }

}
