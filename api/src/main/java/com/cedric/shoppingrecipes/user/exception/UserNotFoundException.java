package com.cedric.shoppingrecipes.user.exception;

public class UserNotFoundException extends RuntimeException{

    private final String userEmail;

    public UserNotFoundException(String userEmail){
        super("User not found: " + userEmail);
        this.userEmail = userEmail;
    }
    public String getUserEmail(){
        return userEmail;
    }
}