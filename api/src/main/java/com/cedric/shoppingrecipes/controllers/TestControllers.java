package com.cedric.shoppingrecipes.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestControllers {

    @GetMapping("/hello")
    public String hello() {
        return  "Hello Cédric, backend OK !!";
    }
}