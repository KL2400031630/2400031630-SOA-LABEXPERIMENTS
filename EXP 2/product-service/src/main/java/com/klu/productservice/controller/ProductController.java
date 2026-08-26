package com.klu.productservice.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/products")
    public String getProducts() {
        return "Product Service - Instance " + port;
    }

    @GetMapping("/products/{id}")
    public String getProduct(@PathVariable int id) {
        return "Product " + id + " - Instance " + port;
    }
}