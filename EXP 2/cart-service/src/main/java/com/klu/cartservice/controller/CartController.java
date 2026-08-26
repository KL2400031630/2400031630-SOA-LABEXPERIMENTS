package com.klu.cartservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {

    @GetMapping("/cart")
    public String getCart() {
        return "Cart Service is running on port 8083";
    }

    @GetMapping("/cart/{id}")
    public String getCartById(@PathVariable int id) {
        return "Cart " + id + " - Cart Service on port 8083";
    }
}