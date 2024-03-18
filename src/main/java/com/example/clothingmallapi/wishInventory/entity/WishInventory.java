package com.example.clothingmallapi.wishInventory.entity;

import com.example.clothingmallapi.item.controller.entity.Item;
import com.example.clothingmallapi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
    private User user;

    private String name;

    @OneToMany
    private List<Item> items;

}
