package com.cedric.shoppingrecipes.user.dto;

public record UserLoginRequest(
    String email,
    String password
) { }