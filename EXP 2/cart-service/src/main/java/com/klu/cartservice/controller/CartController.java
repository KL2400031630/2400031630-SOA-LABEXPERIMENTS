package com.klu.cartservice.controller;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CartController {

    @GetMapping("/cart")
    public List<Map<String, Object>> getCart() {

        return List.of(
                Map.of(
                        "id", 1,
                        "product", "Laptop",
                        "quantity", 1,
                        "price", 75000
                ),
                Map.of(
                        "id", 2,
                        "product", "Wireless Mouse",
                        "quantity", 2,
                        "price", 1200
                )
        );
    }

    @GetMapping("/cart/{id}")
    public Map<String, Object> getCartById(@PathVariable int id) {

        return Map.of(
                "id", id,
                "product", "Product " + id,
                "quantity", 1,
                "price", 999
        );
    }
}