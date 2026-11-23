package com.cedric.shoppingrecipes.auth.service;

import com.cedric.shoppingrecipes.auth.AuthService;
import com.cedric.shoppingrecipes.auth.JwtService;
import com.cedric.shoppingrecipes.auth.exception.EmailAlreadyUsedException;
import com.cedric.shoppingrecipes.auth.exception.InvalidCredentialsException;
import com.cedric.shoppingrecipes.auth.exception.UsernameAlreadyUsedException;
import com.cedric.shoppingrecipes.user.dto.UserLoginRequest;
import com.cedric.shoppingrecipes.user.dto.UserRegisterRequest;
import com.cedric.shoppingrecipes.user.entity.User;
import com.cedric.shoppingrecipes.user.mapper.UserMapper;
import com.cedric.shoppingrecipes.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;

    @InjectMocks private AuthService authService;

    @Test
    void shouldThrowInvalidCredentialsWhenEmailUnknown() {
        when(userRepository.findByEmailIgnoreCase("inconnu@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new UserLoginRequest("inconnu@example.com", "peu-importe")));
    }

    @Test
    void shouldThrowInvalidCredentialsWhenPasswordWrong() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hash-en-base");

        when(userRepository.findByEmailIgnoreCase("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("mauvais", "hash-en-base")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new UserLoginRequest("test@example.com", "mauvais")));
    }

    @Test
    void shouldThrowEmailAlreadyUsedWhenEmailExists() {
        UserRegisterRequest request =
                new UserRegisterRequest("pseudo", "existe@example.com", "Un-mot-de-passe-12!");
        when(userRepository.existsByEmailIgnoreCase("existe@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyUsedException.class,
                () -> authService.register(request));

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowUsernameAlreadyUsedWhenUsernameExists() {
        UserRegisterRequest request =
                new UserRegisterRequest("pseudo", "nouveau@example.com", "Un-mot-de-passe-12!");
        when(userRepository.existsByUsernameIgnoreCase("pseudo")).thenReturn(true);

        assertThrows(UsernameAlreadyUsedException.class,
                () -> authService.register(request));

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}