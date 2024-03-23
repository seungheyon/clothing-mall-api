package com.example.clothingmallapi.item.service;

import com.example.clothingmallapi.item.dto.ItemBulkRequestDto;
import com.example.clothingmallapi.item.dto.ItemRequestDto;
import com.example.clothingmallapi.item.entity.Item;
import com.example.clothingmallapi.item.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void createItem(ItemRequestDto itemRequestDto){
        itemRepository.save(Item.builder()
                .name(itemRequestDto.getName())
                .build());
    }

    @Transactional
    public void createItems(ItemBulkRequestDto itemBulkRequestDto){
        for(ItemRequestDto itemRequestDto : itemBulkRequestDto.getItemList()){
            itemRepository.save(Item.builder()
                    .name(itemRequestDto.getName())
                    .build());
        }
    }

}
