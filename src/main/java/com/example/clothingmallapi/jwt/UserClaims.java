package com.example.clothingmallapi.jwt;

import io.jsonwebtoken.Claims;

public interface UserClaims extends Claims {
    public static final String EMAILID = "EMA";
}
