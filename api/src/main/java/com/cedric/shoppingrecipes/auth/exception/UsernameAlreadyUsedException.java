package com.cedric.shoppingrecipes.auth.exception;

public class UsernameAlreadyUsedException extends RuntimeException {
    public UsernameAlreadyUsedException() {
        super("Ce nom d'utilisateur est déjà utilisé");
    }
}