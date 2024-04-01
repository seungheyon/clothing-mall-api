package com.example.clothingmallapi.users.repository;

import com.example.clothingmallapi.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String username);
    boolean existsByEmailId(String emailId);
    Optional<Users> findByEmailId(String emailId);
}
