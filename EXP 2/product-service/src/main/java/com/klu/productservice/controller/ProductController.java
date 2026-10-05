package com.klu.productservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ProductController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/products")
    public List<Map<String, Object>> getProducts() {

        return List.of(
                Map.of(
                        "id", 1,
                        "name", "Laptop",
                        "price", 75000,
                        "category", "Electronics",
                        "instance", port
                ),
                Map.of(
                        "id", 2,
                        "name", "Wireless Mouse",
                        "price", 1200,
                        "category", "Accessories",
                        "instance", port
                ),
                Map.of(
                        "id", 3,
                        "name", "Mechanical Keyboard",
                        "price", 3500,
                        "category", "Accessories",
                        "instance", port
                ),
                Map.of(
                        "id", 4,
                        "name", "Headphones",
                        "price", 4500,
                        "category", "Audio",
                        "instance", port
                )
        );
    }

    @GetMapping("/products/{id}")
    public Map<String, Object> getProduct(@PathVariable int id) {

        return Map.of(
                "id", id,
                "name", "Product " + id,
                "price", 999,
                "instance", port
        );
    }
}