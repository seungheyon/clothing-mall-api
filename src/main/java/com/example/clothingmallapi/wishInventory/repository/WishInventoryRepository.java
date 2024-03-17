package com.example.clothingmallapi.wishInventory.repository;

import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishInventoryRepository extends JpaRepository<WishInventory, Long> {
}
