package com.example.clothingmallapi.wishInventory.service;

import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryRequestDto;
import com.example.clothingmallapi.wishInventory.repository.WishInventoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WishInventoryServiceTest {

    private final WishInventoryRepository wishInventoryRepository;
    private final UsersRepository userRepository;

    @Autowired
    public WishInventoryServiceTest(WishInventoryRepository wishInventoryRepository, UsersRepository userRepository) {
        this.wishInventoryRepository = wishInventoryRepository;
        this.userRepository = userRepository;
    }

    @DisplayName("sut는 주어진 데이터로 wishInventory 를 생성한다.")
    @Test
    void createWishInventoryTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, userRepository);
        String testUserName = "tester";
        String testUserEmailId = "emailId";
        String testUserPw = "pw";
        Users user = Users.builder()
                .name(testUserName)
                .emailId(testUserEmailId)
                .password(testUserPw)
                .build();
        var createdUser = userRepository.save(user);

        String testWishInventoryName = "testWishInventory";
        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto(testWishInventoryName);

        // Act
        var actual = sut.createWishInventory(createdUser.getId(), wishInventoryRequestDto);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getUser().getId()).isEqualTo(createdUser.getId());
        assertThat(actual.getName()).isEqualTo(testWishInventoryName);

    }

}
