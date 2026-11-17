package com.cedric.shoppingrecipes.auth;

import com.cedric.shoppingrecipes.auth.exception.EmailAlreadyUsedException;
import com.cedric.shoppingrecipes.auth.exception.InvalidCredentialsException;
import com.cedric.shoppingrecipes.user.dto.UserLoginRequest;
import com.cedric.shoppingrecipes.user.dto.UserRegisterRequest;
import com.cedric.shoppingrecipes.user.entity.User;
import com.cedric.shoppingrecipes.user.mapper.UserMapper;
import com.cedric.shoppingrecipes.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private UserMapper userMapper;

    @InjectMocks private AuthService authService;

    @Test
    void shouldThrowInvalidCredentialsWhenEmailUnknown() {
        when(userRepository.findByEmail("inconnu@example.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new UserLoginRequest("inconnu@example.com", "peu-importe")));
    }

    @Test
    void shouldThrowInvalidCredentialsWhenPasswordWrong() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("hash-en-base");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("mauvais", "hash-en-base")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class,
                () -> authService.login(new UserLoginRequest("test@example.com", "mauvais")));
    }

    @Test
    void shouldThrowEmailAlreadyUsedWhenEmailExists() {
        when(userRepository.findByEmail("existe@example.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyUsedException.class,
                () -> authService.register(
                        new UserRegisterRequest("pseudo", "existe@example.com", "Un-mot-de-passe-12!")));
    }
}