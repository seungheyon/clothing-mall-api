package com.example.clothingmallapi.wishInventory.entity;

import com.example.clothingmallapi.item.entity.Item;
import com.example.clothingmallapi.users.entity.Users;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Users user;

    private String name;

    @OneToMany
    @Builder.Default
    private List<Item> items = new ArrayList<>();

    public void addItemToItemList(Item item){
        this.items.add(item);
    }

    public void removeItemFromItemList(Item item){
        this.items.remove(item);
    }

}
