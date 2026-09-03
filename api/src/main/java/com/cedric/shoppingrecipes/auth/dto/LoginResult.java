package com.cedric.shoppingrecipes.auth.dto;

import com.cedric.shoppingrecipes.user.dto.UserResponse;

public record LoginResult (
        String token,
        UserResponse user
) {}
