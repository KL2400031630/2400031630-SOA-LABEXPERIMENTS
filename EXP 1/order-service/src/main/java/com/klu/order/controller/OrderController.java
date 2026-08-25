package com.klu.order.controller;

import com.klu.order.model.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @PostMapping("/orders")
    public String placeOrder(@RequestBody Order order) {

        return "Order placed successfully for user "
                + order.getUserId()
                + " at restaurant "
                + order.getRestaurantId();
    }
}