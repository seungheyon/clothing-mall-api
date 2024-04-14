package com.example.clothingmallapi.users.service;

public interface TokenGenerator {
    String createToken(String loginId);
}
