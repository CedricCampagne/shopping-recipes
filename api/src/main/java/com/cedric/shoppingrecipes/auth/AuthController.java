package com.cedric.shoppingrecipes.auth;

import com.cedric.shoppingrecipes.auth.dto.AuthenticationResponse;
import com.cedric.shoppingrecipes.auth.dto.LoginResult;
import com.cedric.shoppingrecipes.user.dto.UserResponse;
import com.cedric.shoppingrecipes.user.entity.User;
import com.cedric.shoppingrecipes.user.dto.UserLoginRequest;
import com.cedric.shoppingrecipes.user.dto.UserRegisterRequest;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody UserRegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody UserLoginRequest request,
            HttpServletResponse response
    ) {
        LoginResult result = authService.login(request);

        ResponseCookie cookie = ResponseCookie
                .from("access_token", result.token())
                .httpOnly(true)
                .path("/")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return  ResponseEntity.ok(result.user());
    }

    @GetMapping("/me")
    public UserResponse me (
            Authentication authentication
    ) {
        return authService.getCurrentUser(authentication);
    }
}