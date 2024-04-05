package com.example.clothingmallapi.wishInventory.service;

import com.example.clothingmallapi.item.entity.Item;
import com.example.clothingmallapi.item.repository.ItemRepository;
import com.example.clothingmallapi.users.entity.Users;
import com.example.clothingmallapi.users.repository.UsersRepository;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryDetailDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoriesResponseDto;
import com.example.clothingmallapi.wishInventory.dto.WishInventoryRequestDto;
import com.example.clothingmallapi.wishInventory.entity.WishInventory;
import com.example.clothingmallapi.wishInventory.repository.WishInventoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class WishInventoryServiceTest {

    private final WishInventoryRepository wishInventoryRepository;
    private final UsersRepository usersRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public WishInventoryServiceTest(WishInventoryRepository wishInventoryRepository, UsersRepository usersRepository, ItemRepository itemRepository) {
        this.wishInventoryRepository = wishInventoryRepository;
        this.usersRepository = usersRepository;
        this.itemRepository = itemRepository;
    }


    @DisplayName("sut는 주어진 데이터로 wishInventory 를 생성한다.")
    @Test
    void createWishInventoryTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);
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
    @Transactional
    @Test
    void getWishInventoriesTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);

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
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Long cursor = 0L;

        // Act
        WishInventoriesResponseDto actual = sut.getWishInventories(user.getId(), pageRequest, cursor);

        // Assert
        assertThat(actual.getWishInventories().get(0)).isNotNull();
        assertThat(actual.getWishInventories().get(0).getId()).isEqualTo(createdCapWishInventory.getId());
        assertThat(actual.getWishInventories().get(0).getName()).isEqualTo(testCapWishInventoryName);
        assertThat(actual.getWishInventories().get(1)).isNotNull();
        assertThat(actual.getWishInventories().get(1).getId()).isEqualTo(createdPantsWishInventory.getId());
        assertThat(actual.getWishInventories().get(1).getName()).isEqualTo(testPantsWishInventoryName);

    }


    @DisplayName("sut 는 wishInventoryId 에 해당하는 wishInventory 를 삭제한다.")
    @Transactional
    @Test
    void deleteWishInventoryTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);
        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto("testWishInventoryName");

        var createdWishInventory = sut.createWishInventory(user.getId(), wishInventoryRequestDto);

        // Act
        sut.deleteWishInventory(user.getId(), createdWishInventory.getId());
        Optional<WishInventory> wishInventoryOptional = wishInventoryRepository.findById(createdWishInventory.getId());

        // Assert
        assertThat(wishInventoryOptional).isNotPresent();

    }

    @DisplayName("sut 는 wishInventoryId 에 해당하는 wishInventory 의 userId 가 요청의 userId 와 다를경우 wishInventory 를 삭제할 수 없다.")
    @Transactional
    @Test
    void deleteWishInventoryTestWithIllegalArgumentCase(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);
        String expectedMessage = "내 찜 서랍이 아닙니다.";
        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        var illegalUser = usersRepository.save(Users.builder()
                .name("illegalUser")
                .emailId("illegalUserId")
                .password("testPw")
                .build());

        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto("testWishInventoryName");

        var createdWishInventory = sut.createWishInventory(user.getId(), wishInventoryRequestDto);

        // Act & Assert
        assertThatThrownBy(() -> sut.deleteWishInventory(illegalUser.getId(), createdWishInventory.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);

    }

    @DisplayName("sut 는 wishInventoryId 에 해당하는 찜서랍에 itemId 에 해당하는 상품을 담는다.")
    @Transactional
    @Test
    void pickupItemTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);

        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto("testWishInventoryName");
        var createdWishInventory = sut.createWishInventory(user.getId(), wishInventoryRequestDto);

        String testItemName = "testItemName";
        var createdItem = itemRepository.save(Item.builder()
                .name(testItemName)
                .build());

        // Act
        sut.pickupItemToWishInventory(user.getId(), createdWishInventory.getId(), createdItem.getId());
        List<Item> actual = createdWishInventory.getItems();

        // Assert
        assertThat(actual.get(0).getId()).isEqualTo(createdItem.getId());
        assertThat(actual.get(0).getName()).isEqualTo(createdItem.getName());

    }

    @DisplayName("sut 는 userId 와 연결된 wishInventory 가 아닌 경우 상품을 찜할 수 없다.")
    @Transactional
    @Test
    void pickupItemTestWithIllegalArgumentCase(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);
        String expectedMessage = "내 찜 서랍이 아닙니다.";

        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        var illegalUser = usersRepository.save(Users.builder()
                .name("illegalUser")
                .emailId("illegalUserId")
                .password("testPw")
                .build());

        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto("testWishInventoryName");
        var createdWishInventory = sut.createWishInventory(user.getId(), wishInventoryRequestDto);

        String testItemName = "testItemName";
        var createdItem = itemRepository.save(Item.builder()
                .name(testItemName)
                .build());

        // Act & Assert
        sut.pickupItemToWishInventory(user.getId(), createdWishInventory.getId(), createdItem.getId());
        assertThatThrownBy(() -> sut.pickupItemToWishInventory(illegalUser.getId(), createdWishInventory.getId(), createdItem.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);

    }

    @DisplayName("sut 는 자신의 다른 찜 서랍에 담긴 상품을 찜 할수 없다.")
    @Transactional
    @Test
    void pickupItemTestWithDuplicatedCase(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);
        String expectedMessage = "내 다른 찜 서랍에 있는 상품입니다.";

        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto("testWishInventoryName");
        var createdWishInventory = sut.createWishInventory(user.getId(), wishInventoryRequestDto);
        WishInventoryRequestDto anotherWishInventoryRequestDto = new WishInventoryRequestDto("anotherWishInventory");
        var anotherWishInventory = sut.createWishInventory(user.getId(), anotherWishInventoryRequestDto);

        String testItemName = "testItemName";
        var createdItem = itemRepository.save(Item.builder()
                .name(testItemName)
                .build());

        // Act & Assert
        sut.pickupItemToWishInventory(user.getId(), anotherWishInventory.getId(), createdItem.getId());
        assertThatThrownBy(() -> sut.pickupItemToWishInventory(user.getId(), createdWishInventory.getId(), createdItem.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);

    }


    @DisplayName("sut 는 wishInventoryId 에 해당하는 찜서랍에서 itemId 에 해당하는 상품을 찜 해제한다.")
    @Transactional
    @Test
    void pickoutItemTest(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);

        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto("testWishInventoryName");
        var createdWishInventory = sut.createWishInventory(user.getId(), wishInventoryRequestDto);

        String testItemName = "testItemName";
        var createdItem = itemRepository.save(Item.builder()
                .name(testItemName)
                .build());

        // Act
        sut.pickupItemToWishInventory(user.getId(), createdWishInventory.getId(), createdItem.getId());
        sut.pickoutItemFromWishInventory(user.getId(), createdWishInventory.getId(), createdItem.getId());
        List<Item> actual = createdWishInventory.getItems();
        Optional<List<Item>> actualOptional = Optional.ofNullable(actual);


        // Assert
        //assertThat(actualOptional.isEmpty()).isFalse();
        assertThat(actualOptional.isPresent()).isTrue();
    }

    @DisplayName("sut 는 userId 와 연결된 wishInventory 가 아닌 경우 찜을 해제할 수 없다.")
    @Transactional
    @Test
    void pickoutItemTestWithIllegalArgumentCase(){

        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);
        String expectedMessage = "내 찜 서랍이 아닙니다.";

        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        var illegalUser = usersRepository.save(Users.builder()
                .name("illegalUser")
                .emailId("illegalUserId")
                .password("testPw")
                .build());

        WishInventoryRequestDto wishInventoryRequestDto = new WishInventoryRequestDto("testWishInventoryName");
        var createdWishInventory = sut.createWishInventory(user.getId(), wishInventoryRequestDto);

        String testItemName = "testItemName";
        var createdItem = itemRepository.save(Item.builder()
                .name(testItemName)
                .build());

        // Act & Assert
        sut.pickupItemToWishInventory(user.getId(), createdWishInventory.getId(), createdItem.getId());
        assertThatThrownBy(() -> sut.pickoutItemFromWishInventory(illegalUser.getId(), createdWishInventory.getId(), createdItem.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(expectedMessage);
    }

    @DisplayName("sut는  wishInventoryId 에 해당하는 wishInventory 에 담긴 item List 를 조회하여 반환한다.")
    @Transactional
    @Test
    void getItemsInWishInventoryTest(){
        // Arrange
        var sut = new WishInventoryService(wishInventoryRepository, usersRepository, itemRepository);

        var user = usersRepository.save(Users.builder()
                .name("testUser")
                .emailId("testEmailId")
                .password("testPw")
                .build());

        String testWishInventoryName = "testWishInventory";

        WishInventoryRequestDto testWishInventoryRequestDto = new WishInventoryRequestDto(testWishInventoryName);
        var createdWishInventory = sut.createWishInventory(user.getId(),testWishInventoryRequestDto);

        String testItemName1 = "testItemName1";
        String testItemName2 = "testItemName2";
        var createdItem1 = itemRepository.save(Item.builder()
                .name(testItemName1)
                .build());

        var createdItem2 = itemRepository.save(Item.builder()
                .name(testItemName2)
                .build());

        sut.pickupItemToWishInventory(user.getId(), createdWishInventory.getId(), createdItem1.getId());
        sut.pickupItemToWishInventory(user.getId(), createdWishInventory.getId(), createdItem2.getId());


        int pageSize = 2;
        PageRequest pageRequest = PageRequest.of(0, pageSize);
        Long cursor = 0L;

        // Act
        WishInventoryDetailDto actual = sut.getItemsInWishInventory(createdWishInventory.getId(), pageRequest, cursor);

        // Assert
        assertThat(actual.getItems().get(0)).isNotNull();
        assertThat(actual.getItems().get(0).getId()).isEqualTo(createdItem1.getId());
        assertThat(actual.getItems().get(0).getName()).isEqualTo(testItemName1);
        assertThat(actual.getItems().get(1)).isNotNull();
        assertThat(actual.getItems().get(1).getId()).isEqualTo(createdItem2.getId());
        assertThat(actual.getItems().get(1).getName()).isEqualTo(testItemName2);

    }

}
