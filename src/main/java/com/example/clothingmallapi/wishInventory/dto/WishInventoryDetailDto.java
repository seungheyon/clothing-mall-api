package com.example.clothingmallapi.wishInventory.dto;

import com.example.clothingmallapi.item.dto.ItemResponseDto;
import com.example.clothingmallapi.item.entity.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class WishInventoryDetailDto {
    private List<ItemResponseDto> items = new ArrayList<>();
    private Long cursor;

    public WishInventoryDetailDto(List<Item> itemList, Long cursor){
        for(Item item : itemList){
            this.items.add(new ItemResponseDto(item.getId(), item.getName()));
        }
        this.cursor = cursor;
    }
}
