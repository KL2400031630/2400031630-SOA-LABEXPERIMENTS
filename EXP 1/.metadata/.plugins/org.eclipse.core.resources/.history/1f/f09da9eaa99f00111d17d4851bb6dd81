package com.klu.order.controller;

import java.util.List;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
public class RestaurantDiscoveryController {

    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    public RestaurantDiscoveryController(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        this.restClient = RestClient.create();
    }

    @GetMapping("/orders/restaurants")
    public String getRestaurantsThroughEureka() {

        List<ServiceInstance> instances =
                discoveryClient.getInstances("restaurant-service");

        if (instances.isEmpty()) {
            return "Restaurant Service is not available";
        }

        ServiceInstance instance = instances.get(0);

        String url = instance.getUri() + "/restaurants";

        return restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }
}