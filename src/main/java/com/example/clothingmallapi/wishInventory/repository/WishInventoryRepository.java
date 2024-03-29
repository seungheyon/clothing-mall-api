package com.example.clothingmallapi.wishInventory.repository;

import com.example.clothingmallapi.item.entity.Item;
import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishInventoryRepository extends JpaRepository<WishInventory, Long> {
    List<WishInventory> findAllByUserId(Long userId, Pageable pageable);
    List<WishInventory> findAllByUserIdAndIdGreaterThanOrderById(Long userId, Long Id, Pageable pageable);


    @Query("SELECT DISTINCT i FROM WishInventory wi JOIN wi.items i WHERE wi.id = :wishInventoryId AND i.id > :cursorId")
    List<Item> findItemsByWishInventoryIdAndCursor(
            @Param("wishInventoryId") Long wishInventoryId,
            @Param("cursorId") Long cursor,
            Pageable pageable);
    boolean existsByUserIdAndName(Long userId, String name);
}
