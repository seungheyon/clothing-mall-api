package com.example.clothingmallapi.item.repository;

import com.example.clothingmallapi.item.controller.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
}
