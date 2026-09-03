package com.cedric.shoppingrecipes.auth;

import com.cedric.shoppingrecipes.auth.dto.AuthenticationResponse;
import com.cedric.shoppingrecipes.auth.dto.LoginResult;
import com.cedric.shoppingrecipes.user.CustomUserDetails;
import com.cedric.shoppingrecipes.user.dto.UserResponse;
import com.cedric.shoppingrecipes.user.entity.User;
import com.cedric.shoppingrecipes.user.exception.UserNotFoundException;
import com.cedric.shoppingrecipes.user.repository.UserRepository;
import com.cedric.shoppingrecipes.user.dto.UserLoginRequest;
import com.cedric.shoppingrecipes.user.dto.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public User register(UserRegisterRequest request) {
        // Verification email déjà présent
        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new RuntimeException("Email dèjà utilisé");
        }

        String hashed = passwordEncoder.encode(request.password());

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(hashed);

        return userRepository.save(user);
    }

    public LoginResult login(UserLoginRequest request) {
        // Vérifier si le user existe
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Email inconnu")) ;

        // Vérifier le mot de pass
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new RuntimeException("Mot de passe incorrect");
        }

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );

        String token = jwtService.generateToken(user);

        LoginResult response = new LoginResult(
                token,
                userResponse
        );

        return response;
    }

    public UserResponse getCurrentUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        String email = userDetails.getUsername();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        UserResponse currentUser = new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );

        return currentUser;
    }
}