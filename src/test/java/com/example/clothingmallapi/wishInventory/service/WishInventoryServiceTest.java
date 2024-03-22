package com.example.clothingmallapi.wishInventory.service;

import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryRequestDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryResponseDto;
import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import com.example.clothingmallapi.wishInventory.repository.WishInventoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class WishInventoryServiceTest {

    private final WishInventoryRepository wishInventoryRepository;
    private final UsersRepository usersRepository;

    @Autowired
    public WishInventoryServiceTest(WishInventoryRepository wishInventoryRepository, UsersRepository userRepository) {
        this.wishInventoryRepository = wishInventoryRepository;
        this.usersRepository = userRepository;
    }

    @DisplayName("sut는 주어진 데이터로 wishInventory 를 생성한다.")
    @Test
    void createWishInventoryTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository);
        String testUserName = "tester";
        String testUserEmailId = "emailId";
        String testUserPw = "pw";
        var createdUser = usersRepository.save(Users.builder()
                .name(testUserName)
                .emailId(testUserEmailId)
                .password(testUserPw)
                .build());

        String testWishInventoryName = "testWishInventory";
        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto(testWishInventoryName);

        // Act
        var actual = sut.createWishInventory(createdUser.getId(), wishInventoryRequestDto);

        // Assert
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getUser().getId()).isEqualTo(createdUser.getId());
        assertThat(actual.getName()).isEqualTo(testWishInventoryName);

    }


    @DisplayName("sut는  userId 에 해당하는 wishInventory 목록을 조회하여 반환한다.")
    @Test
    void getWishInventoriesTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository);

        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        String testCapWishInventoryName = "testCapWishInventory";
        String testPantsWishInventoryName = "testPantsWishInventory";
        WishInventoryRequestDto capWishInventoryRequestDto = new WishInventoryRequestDto(testCapWishInventoryName);
        var createdCapWishInventory = sut.createWishInventory(user.getId(),capWishInventoryRequestDto);
        WishInventoryRequestDto pantsWishInventoryRequestDto = new WishInventoryRequestDto(testPantsWishInventoryName);
        var createdPantsWishInventory = sut.createWishInventory(user.getId(),pantsWishInventoryRequestDto);

        int pageSize = 2;
        PageRequest pageRequest = PageRequest.of(0, pageSize, Sort.by("id"));

        // Act
        List<WishInventoryResponseDto> actual = sut.getWishInventories(user.getId(), pageRequest);

        // Assert
        assertThat(actual.get(0)).isNotNull();
        assertThat(actual.get(0).getId()).isEqualTo(createdCapWishInventory.getId());
        assertThat(actual.get(0).getName()).isEqualTo(testCapWishInventoryName);
        assertThat(actual.get(1)).isNotNull();
        assertThat(actual.get(1).getId()).isEqualTo(createdPantsWishInventory.getId());
        assertThat(actual.get(1).getName()).isEqualTo(testPantsWishInventoryName);

    }


    @DisplayName("sut 는 wishInventoryId 에 해당하는 wishInventory 를 삭제한다.")
    @Test
    void deleteWishInventoryTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository);
        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto("testWishInventoryName");

        var createdWishInventory = sut.createWishInventory(user.getId(), wishInventoryRequestDto);

        // Act
        sut.deleteWishInventory(createdWishInventory.getId());
        Optional<WishInventory> wishInventoryOptional = wishInventoryRepository.findById(createdWishInventory.getId());

        // Assert
        assertThat(wishInventoryOptional).isNotPresent();

    }

}
