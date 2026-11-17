package com.cedric.shoppingrecipes.auth.exception;

public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException( ) {
        super("Cet email est déjà utilisé");
    }
}
