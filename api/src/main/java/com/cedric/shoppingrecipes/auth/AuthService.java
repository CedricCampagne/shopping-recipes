package com.cedric.shoppingrecipes.auth;

import com.cedric.shoppingrecipes.auth.dto.AuthenticationResponse;
import com.cedric.shoppingrecipes.user.entity.User;
import com.cedric.shoppingrecipes.user.repository.UserRepository;
import com.cedric.shoppingrecipes.user.dto.UserLoginRequest;
import com.cedric.shoppingrecipes.user.dto.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public String login(UserLoginRequest request) {
        // Vérifier si le user existe
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Email inconnu")) ;

        // Vérifier le mot de pass
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new RuntimeException("Mot de passe incorrect");
        }

        String token = jwtService.generateToken(user);
        return token;
    }
}