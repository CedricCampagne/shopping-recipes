package com.cedric.shoppingrecipes.user.mapper;

import com.cedric.shoppingrecipes.user.dto.UserResponse;
import com.cedric.shoppingrecipes.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {

    public UserResponse toUserResponse(User user){
        return  new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
    }
}
