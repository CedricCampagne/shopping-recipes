package com.cedric.shoppingrecipes.auth;

import com.cedric.shoppingrecipes.auth.dto.LoginResult;

import com.cedric.shoppingrecipes.auth.exception.EmailAlreadyUsedException;
import com.cedric.shoppingrecipes.auth.exception.InvalidCredentialsException;
import com.cedric.shoppingrecipes.auth.exception.UsernameAlreadyUsedException;
import com.cedric.shoppingrecipes.user.dto.UserResponse;
import com.cedric.shoppingrecipes.user.dto.UserLoginRequest;
import com.cedric.shoppingrecipes.user.dto.UserRegisterRequest;
import com.cedric.shoppingrecipes.user.CustomUserDetails;
import com.cedric.shoppingrecipes.user.entity.User;
import com.cedric.shoppingrecipes.user.exception.UserNotFoundException;
import com.cedric.shoppingrecipes.user.mapper.UserMapper;
import com.cedric.shoppingrecipes.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public UserResponse register(UserRegisterRequest request) {
        // Verification email déjà présent
        if (userRepository.findByEmail(request.email()).isPresent()){
            throw new EmailAlreadyUsedException();
        }

        // Verification username déjà présent
        if(userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyUsedException();
        }

        String hashed = passwordEncoder.encode(request.password());

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(hashed);

        User saved =  userRepository.save(user);

        return userMapper.toUserResponse(saved);
    }

    public LoginResult login(UserLoginRequest request) {
        // Vérifier si le user existe
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        // Vérifier le mot de pass
        if(!passwordEncoder.matches(request.password(), user.getPassword())){
            throw new InvalidCredentialsException();
        }

        UserResponse userResponse = userMapper.toUserResponse(user);

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

        UserResponse currentUser = userMapper.toUserResponse(user);

        return currentUser;
    }
}