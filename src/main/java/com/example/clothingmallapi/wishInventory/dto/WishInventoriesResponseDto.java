package com.example.clothingmallapi.wishInventory.dto;

import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor

public class WishInventoriesResponseDto {
    private List<WishInventoryResponseDto> wishInventories = new ArrayList<>();
    private Long cursor;

    public WishInventoriesResponseDto(List<WishInventory> wishInventoryList, Long cursor){
        for(WishInventory wishInventory : wishInventoryList){
            this.wishInventories.add(new WishInventoryResponseDto(wishInventory.getId(), wishInventory.getName()));
        }
        this.cursor = cursor;
    }
}
