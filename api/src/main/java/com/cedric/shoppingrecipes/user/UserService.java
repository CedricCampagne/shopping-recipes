package com.cedric.shoppingrecipes.user;

import com.cedric.shoppingrecipes.user.dto.UserLoginRequest;
import com.cedric.shoppingrecipes.user.dto.UserRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User register(UserRegisterRequest request) {
        // Verification email déjà présent
        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new RuntimeException("Email dèjà utilisé");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        return userRepository.save(user);
    }

    public User login(UserLoginRequest request) {
        // Vérifier si le user existe
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Email inconnu")) ;

        // Vérifier le mot de pass
        if(!user.getPassword().equals(request.password())){
            throw new RuntimeException("Mot de passe incorrect");
        }

        return  user;
    }
}