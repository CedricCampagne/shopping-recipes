package com.cedric.shoppingrecipes.auth.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token
) { }
