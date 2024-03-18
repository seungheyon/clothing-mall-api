package com.example.clothingmallapi.item.controller;

import com.example.clothingmallapi.item.dto.ItemBulkRequestDto;
import com.example.clothingmallapi.item.dto.ItemRequestDto;
import com.example.clothingmallapi.item.service.ItemService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/items")
    public void createItem(@RequestBody ItemRequestDto itemRequestDto){
        itemService.createItem(itemRequestDto);
    }

    @PostMapping("/items/bulk")
    public void createItems(@RequestBody ItemBulkRequestDto itemBulkRequestDto){
        itemService.createItems(itemBulkRequestDto);
    }

}
