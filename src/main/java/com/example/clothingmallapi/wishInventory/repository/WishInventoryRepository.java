package com.example.clothingmallapi.wishInventory.repository;

import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishInventoryRepository extends JpaRepository<WishInventory, Long> {
    List<WishInventory> findAllByUserId(Long userId, Pageable pageable);
}
