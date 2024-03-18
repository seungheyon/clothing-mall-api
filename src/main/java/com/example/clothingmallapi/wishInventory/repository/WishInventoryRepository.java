package com.example.clothingmallapi.wishInventory.repository;

import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishInventoryRepository extends JpaRepository<WishInventory, Long> {
    public List<WishInventory> findAllByUserId(Long userId);
}
