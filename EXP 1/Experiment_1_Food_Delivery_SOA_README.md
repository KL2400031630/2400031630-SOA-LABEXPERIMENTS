# Food Delivery SOA System --- Experiment 1

## Course

-   **Course:** SOA Programming and Microservices
-   **Academic Year:** 2026--27
-   **Course Code:** 24SDCS03 A/R
-   **Experiment:** Skill Experiment 1 --- Designing a Food Delivery SOA
    System

------------------------------------------------------------------------

# 1. Experiment Overview

This experiment demonstrates how a monolithic food-delivery application
can be separated into independent services using Service-Oriented
Architecture (SOA).

The original problem is that a monolithic application tightly couples:

-   Restaurant listing
-   Order processing
-   Customer-related functionality

As the application grows, small changes can affect the whole system,
deployment becomes risky, and individual components cannot be scaled
independently.

For this experiment, we separate the application into two services:

``` text
                    Food Delivery SOA
                           |
             +-------------+-------------+
             |                           |
             v                           v
      Restaurant Service            Order Service
          :8081                        :8082
             |                           |
             +----------- REST ------------+
```

The two main services are:

1.  **Restaurant Service** --- manages restaurant data.
2.  **Order Service** --- handles customer orders.

The final implementation uses:

-   Spring Tools Suite (STS)
-   Spring Boot
-   Maven
-   Java 21
-   PostgreSQL 16.15
-   Spring Data JPA
-   REST APIs
-   Postman

------------------------------------------------------------------------

# 2. Important Note About Eureka

During development, Eureka Server and Eureka Client were also configured
and tested.

The Eureka setup was useful for understanding service registration and
service discovery:

``` text
Eureka Server
localhost:8761
       |
       +---- RESTAURANT-SERVICE :8081
       |
       +---- ORDER-SERVICE :8082
```

However, the Experiment 1 instructions specifically require:

-   Restaurant Service
-   Order Service
-   JSON contracts
-   `/restaurants`
-   `/orders`
-   Basic REST APIs
-   SOA documentation
-   SOA vs Microservices comparison

Therefore, **Eureka is not necessary to demonstrate the core
requirements of Experiment 1**.

The Eureka work can be kept as additional learning for later
experiments.

------------------------------------------------------------------------

# 3. Software and Versions

The environment used during development was:

``` text
Java                  21
Spring Boot           4.1.1
Spring Cloud          2025.1.2
PostgreSQL             16.15
Build Tool             Maven
IDE                    Spring Tools Suite (STS)
API Testing            Postman
```

------------------------------------------------------------------------

# 4. Project Structure

The final workspace contains:

``` text
EXP 1
|
+-- eureka-server
|
+-- restaurant-service
|
+-- order-service
```

For the PostgreSQL-backed Experiment 1, the important services are:

``` text
restaurant-service
order-service
```

------------------------------------------------------------------------

# 5. Step 1 --- Create the Restaurant Service

## 5.1 Create a Spring Starter Project

In Spring Tools:

``` text
File
 -> New
 -> Spring Starter Project
```

Use:

``` text
Name:          restaurant-service
Type:          Maven
Packaging:     Jar
Java Version:  21
Language:      Java
Group:         com.klu
Artifact:      restaurant-service
Package:       com.klu.restaurant
```

### Why the package matters

The package should match the service.

For Restaurant Service:

``` text
com.klu.restaurant
```

For Order Service:

``` text
com.klu.order
```

Do not accidentally use `com.klu.restaurant` for the Order Service.

------------------------------------------------------------------------

# 6. Step 2 --- Create the Order Service

Create another Spring Starter Project.

Use:

``` text
Name:          order-service
Type:          Maven
Packaging:     Jar
Java Version:  21
Language:      Java
Group:         com.klu
Artifact:      order-service
Package:       com.klu.order
```

------------------------------------------------------------------------

# 7. Step 3 --- Basic Service Ports

Use separate ports because both services run on the same computer:

``` text
Eureka Server       8761
Restaurant Service  8081
Order Service       8082
```

For the actual Experiment 1 REST demonstration, the important ports are:

``` text
Restaurant Service  -> 8081
Order Service       -> 8082
```

------------------------------------------------------------------------

# 8. Step 4 --- Original Restaurant REST API

The first version of the Restaurant Service returned sample/mock data.

The required endpoint is:

``` text
GET /restaurants
```

Example URL:

``` text
http://localhost:8081/restaurants
```

The response format is:

``` json
[
  {
    "id": 1,
    "name": "Pizza Palace",
    "location": "Bangalore"
  },
  {
    "id": 2,
    "name": "Burger House",
    "location": "Hyderabad"
  },
  {
    "id": 3,
    "name": "Spice Garden",
    "location": "Chennai"
  }
]
```

This proved that the Restaurant Service REST API was working.

------------------------------------------------------------------------

# 9. Step 5 --- Original Order REST API

The required endpoint is:

``` text
POST /orders
```

Example URL:

``` text
http://localhost:8082/orders
```

Request body:

``` json
{
  "userId": 101,
  "restaurantId": 1,
  "items": [
    "Pizza",
    "Burger"
  ]
}
```

The first version returned a confirmation such as:

``` text
Order placed successfully for user 101 at restaurant 1
```

This proved that the Order Service REST API was working.

------------------------------------------------------------------------

# 10. Step 6 --- Test the Basic APIs Using Postman

## Restaurant Service

Method:

``` text
GET
```

URL:

``` text
http://localhost:8081/restaurants
```

Expected:

``` text
200 OK
```

with restaurant JSON.

------------------------------------------------------------------------

## Order Service

Method:

``` text
POST
```

URL:

``` text
http://localhost:8082/orders
```

Body:

``` json
{
  "userId": 101,
  "restaurantId": 1,
  "items": [
    "Pizza",
    "Burger"
  ]
}
```

Expected:

``` text
200 OK
```

with an order confirmation.

------------------------------------------------------------------------

# 11. Step 7 --- PostgreSQL Requirement

The faculty later required that the application use PostgreSQL and that
operations update the database.

PostgreSQL version used:

``` text
PostgreSQL 16.15
```

The PostgreSQL username used was:

``` text
postgres
```

Never put your real database password into a public README or Git
repository.

------------------------------------------------------------------------

# 12. Step 8 --- Create the PostgreSQL Database

Open Command Prompt and connect:

``` bash
psql -U postgres
```

Enter your PostgreSQL password.

Then create the database:

``` sql
CREATE DATABASE food_delivery;
```

Connect to it:

``` sql
\c food_delivery
```

The prompt should become:

``` text
food_delivery=#
```

At this stage, do not manually create the application tables. Spring
Data JPA/Hibernate can create/update them from the Java entities.

------------------------------------------------------------------------

# 13. Step 9 --- Add PostgreSQL and JPA to Restaurant Service

Open:

``` text
restaurant-service/pom.xml
```

The important dependencies are:

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webmvc</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

After changing `pom.xml`:

``` text
Right-click project
 -> Maven
 -> Update Project
```

Wait for Maven to download the dependencies.

------------------------------------------------------------------------

# 14. Step 10 --- Configure Restaurant Service for PostgreSQL

Open:

``` text
restaurant-service/src/main/resources/application.properties
```

Use:

``` properties
spring.application.name=restaurant-service

server.port=8081

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/food_delivery
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Replace:

``` text
YOUR_POSTGRES_PASSWORD
```

with your local PostgreSQL password.

Do not commit the password to GitHub.

A better production practice is to use environment variables or a
secrets manager.

------------------------------------------------------------------------

# 15. Step 11 --- Convert Restaurant to a JPA Entity

File:

``` text
restaurant-service
 -> src/main/java
 -> com.klu.restaurant
 -> model
 -> Restaurant.java
```

Code:

``` java
package com.klu.restaurant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String location;

    public Restaurant() {
    }

    public Restaurant(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public Restaurant(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
```

Important annotations:

``` text
@Entity
```

Marks the class as a database entity.

``` text
@Id
```

Marks the primary key.

``` text
@GeneratedValue(strategy = GenerationType.IDENTITY)
```

Allows PostgreSQL to generate the ID.

------------------------------------------------------------------------

# 16. Step 12 --- Create RestaurantRepository

Create:

``` text
com.klu.restaurant.repository
```

Then:

``` text
RestaurantRepository.java
```

Code:

``` java
package com.klu.restaurant.repository;

import com.klu.restaurant.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
}
```

This provides database operations such as:

``` text
save()
findAll()
findById()
delete()
```

without manually writing SQL for basic operations.

------------------------------------------------------------------------

# 17. Step 13 --- Update RestaurantController

Replace the mock-data controller with:

``` java
package com.klu.restaurant.controller;

import com.klu.restaurant.model.Restaurant;
import com.klu.restaurant.repository.RestaurantRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/restaurants")
    public List<Restaurant> getRestaurants() {
        return restaurantRepository.findAll();
    }

    @PostMapping("/restaurants")
    public Restaurant addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }
}
```

Now the flow is:

``` text
POST /restaurants
      |
      v
RestaurantController
      |
      v
RestaurantRepository
      |
      v
PostgreSQL
```

And:

``` text
GET /restaurants
      |
      v
RestaurantController
      |
      v
RestaurantRepository
      |
      v
PostgreSQL
      |
      v
Current restaurant data
```

------------------------------------------------------------------------

# 18. Step 14 --- Verify Restaurant Table

Start the Restaurant Service.

Hibernate should create/update the table.

In PostgreSQL:

``` sql
\dt
```

You should see a table similar to:

``` text
restaurant
```

Check its data:

``` sql
SELECT * FROM restaurant;
```

Initially it may show:

``` text
(0 rows)
```

That is normal.

------------------------------------------------------------------------

# 19. Step 15 --- Insert Restaurant Data from Postman

Postman request:

``` text
POST http://localhost:8081/restaurants
```

Body:

``` json
{
  "name": "Domino's",
  "location": "Bangalore"
}
```

The response should contain a generated ID, for example:

``` json
{
  "id": 1,
  "name": "Domino's",
  "location": "Bangalore"
}
```

Check PostgreSQL:

``` sql
SELECT * FROM restaurant;
```

The new row should be present.

------------------------------------------------------------------------

# 20. Step 16 --- Verify GET Reads PostgreSQL

Send:

``` text
GET http://localhost:8081/restaurants
```

The response should contain the restaurant that was inserted into
PostgreSQL.

Add another restaurant, for example:

``` json
{
  "name": "Paradise",
  "location": "Hyderabad"
}
```

Then call:

``` text
GET http://localhost:8081/restaurants
```

Both records should be returned.

This demonstrates that the API is using current database data instead of
a hard-coded Java list.

------------------------------------------------------------------------

# 21. Step 17 --- Add PostgreSQL to Order Service

Open:

``` text
order-service/pom.xml
```

Add:

``` xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

Then:

``` text
Maven
 -> Update Project
```

------------------------------------------------------------------------

# 22. Step 18 --- Configure Order Service

File:

``` text
order-service/src/main/resources/application.properties
```

Use:

``` properties
spring.application.name=order-service

server.port=8082

# Eureka
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/food_delivery
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Use the same local PostgreSQL password.

------------------------------------------------------------------------

# 23. Step 19 --- Convert Order to a JPA Entity

The simple implementation stores the list of items as a string in the
database.

File:

``` text
order-service
 -> src/main/java
 -> com.klu.order
 -> model
 -> Order.java
```

Code:

``` java
package com.klu.order.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int userId;

    private int restaurantId;

    private String items;

    public Order() {
    }

    public Order(int userId, int restaurantId, String items) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
```

------------------------------------------------------------------------

# 24. Step 20 --- Create OrderRepository

Create:

``` text
com.klu.order.repository
```

Then:

``` text
OrderRepository.java
```

Code:

``` java
package com.klu.order.repository;

import com.klu.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
```

------------------------------------------------------------------------

# 25. Step 21 --- Update OrderController

The API still accepts the required JSON array for `items`, but the
simple database model stores it as comma-separated text.

Code:

``` java
package com.klu.order.controller;

import com.klu.order.model.Order;
import com.klu.order.repository.OrderRepository;

import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping("/orders")
    public Order placeOrder(@RequestBody OrderRequest request) {

        String items = String.join(",", request.getItems());

        Order order = new Order(
                request.getUserId(),
                request.getRestaurantId(),
                items
        );

        return orderRepository.save(order);
    }

    public static class OrderRequest {

        private int userId;
        private int restaurantId;
        private java.util.List<String> items;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(int restaurantId) {
            this.restaurantId = restaurantId;
        }

        public java.util.List<String> getItems() {
            return items;
        }

        public void setItems(java.util.List<String> items) {
            this.items = items;
        }
    }
}
```

------------------------------------------------------------------------

# 26. Step 22 --- Test Order Persistence

Start Order Service.

Postman:

``` text
POST http://localhost:8082/orders
```

Body:

``` json
{
  "userId": 101,
  "restaurantId": 1,
  "items": [
    "Pizza",
    "Burger"
  ]
}
```

The service should save the order in PostgreSQL and return the saved
order with a generated ID.

Check PostgreSQL using:

``` sql
\dt
```

Then inspect the order table.

Depending on the exact Hibernate naming behavior, the generated table
name may be `order` or another Hibernate-safe representation. If `order`
is used as a SQL reserved word in your PostgreSQL/Hibernate setup,
inspect the actual table name shown by `\dt`.

------------------------------------------------------------------------

# 27. Final Data Flow

After PostgreSQL integration, the important architecture is:

``` text
                    FOOD DELIVERY SOA
                           |
              +------------+------------+
              |                         |
              v                         v
       Restaurant Service         Order Service
           :8081                     :8082
              |                         |
              v                         v
       RestaurantRepository       OrderRepository
              |                         |
              +------------+------------+
                           |
                           v
                    PostgreSQL
                    food_delivery
```

Conceptually, each service owns its own business functionality.

For a simple academic setup, both services currently use the same
PostgreSQL database. In a production microservices architecture,
separate databases or schemas per service would commonly be considered
to maintain stronger data ownership.

------------------------------------------------------------------------

# 28. Final API Contracts

## Restaurant Service

### GET all restaurants

``` text
GET /restaurants
```

Example:

``` text
http://localhost:8081/restaurants
```

Response:

``` json
[
  {
    "id": 1,
    "name": "Domino's",
    "location": "Bangalore"
  }
]
```

### Add restaurant

``` text
POST /restaurants
```

Request:

``` json
{
  "name": "Domino's",
  "location": "Bangalore"
}
```

------------------------------------------------------------------------

## Order Service

### Place order

``` text
POST /orders
```

Request:

``` json
{
  "userId": 101,
  "restaurantId": 1,
  "items": [
    "Pizza",
    "Burger"
  ]
}
```

The API accepts `items` as an array, while the simple PostgreSQL entity
stores the items as comma-separated text.

------------------------------------------------------------------------

# 29. How to Demonstrate the Experiment to Faculty

Start:

``` text
PostgreSQL
Restaurant Service :8081
Order Service      :8082
```

Eureka may also be running if you kept the earlier setup, but it is not
required for the core Experiment 1 demonstration.

## Demonstration 1 --- Restaurant

Postman:

``` text
POST http://localhost:8081/restaurants
```

Body:

``` json
{
  "name": "Domino's",
  "location": "Bangalore"
}
```

Show the generated ID.

Then:

``` text
GET http://localhost:8081/restaurants
```

Show that the new restaurant is returned.

Then optionally show PostgreSQL:

``` sql
SELECT * FROM restaurant;
```

------------------------------------------------------------------------

## Demonstration 2 --- Order

Postman:

``` text
POST http://localhost:8082/orders
```

Body:

``` json
{
  "userId": 101,
  "restaurantId": 1,
  "items": [
    "Pizza",
    "Burger"
  ]
}
```

Show the saved response.

Then show the corresponding PostgreSQL row.

------------------------------------------------------------------------

# 30. What to Say During Viva

### What is SOA?

> SOA, or Service-Oriented Architecture, separates application
> functionality into independent services that communicate through
> defined service contracts.

### Why did you create two services?

> Restaurant management and order processing are separate business
> functionalities, so they can be represented as independent services.

### What is a service contract?

> A service contract defines the data format and communication expected
> between a client and a service. In this experiment, JSON is used as
> the data format.

### What is an endpoint?

> An endpoint is the access point through which a client communicates
> with a service. For example, `/restaurants` and `/orders`.

### Why use PostgreSQL?

> PostgreSQL provides persistent storage so that data submitted through
> the REST APIs is stored in a database instead of being kept only in
> memory.

### What happens when a restaurant is added?

> The POST request reaches the Restaurant Controller, the controller
> calls the JPA repository, Hibernate persists the entity, and
> PostgreSQL stores the row.

### What happens when `/restaurants` is requested?

> The controller calls `restaurantRepository.findAll()`, which retrieves
> the current restaurant records from PostgreSQL and returns them as
> JSON.

### What is the difference between SOA and Microservices?

  ---------------------------------------------------------------------------
  Aspect                  SOA                         Microservices
  ----------------------- --------------------------- -----------------------
  Granularity             Services can be broader     Usually smaller,
                          business services           focused services

  Deployment              Can have more centralized   Services are generally
                          governance/infrastructure   independently
                                                      deployable

  Communication           Often enterprise            Commonly lightweight
                          integration mechanisms      APIs and messaging
  ---------------------------------------------------------------------------

------------------------------------------------------------------------

# 31. Common Problems and Fixes

## Problem: Java installation error in STS

If STS says:

``` text
Cannot find a Java installation on your machine
```

check that Java 21 is installed and configured in STS.

The project was ultimately configured with:

``` text
Java 21
```

------------------------------------------------------------------------

## Problem: Package mismatch

Make sure:

``` text
Restaurant Service
com.klu.restaurant
```

and:

``` text
Order Service
com.klu.order
```

The package declaration at the top of each Java file must match its
folder.

------------------------------------------------------------------------

## Problem: PostgreSQL connection error

Check:

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/food_delivery
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD
```

Also make sure PostgreSQL is running.

------------------------------------------------------------------------

## Problem: Table does not exist

Check:

``` properties
spring.jpa.hibernate.ddl-auto=update
```

Then restart the application.

Check PostgreSQL:

``` sql
\c food_delivery
\dt
```

------------------------------------------------------------------------

## Problem: Port already in use

Check that:

``` text
8081 -> Restaurant Service
8082 -> Order Service
8761 -> Eureka
```

Only one application instance should use a given port at the same time.

------------------------------------------------------------------------

## Problem: Eureka `/actuator/info` returns 404

If you see:

``` text
/actuator/info
404 Not Found
```

that does not mean the Restaurant or Order REST APIs are broken.

For Experiment 1, test the actual required endpoints:

``` text
GET  /restaurants
POST /orders
```

------------------------------------------------------------------------

# 32. Final Checklist

Before considering Experiment 1 complete:

``` text
[ ] Spring Tools installed and workspace opened
[ ] Java 21 configured
[ ] restaurant-service created
[ ] order-service created
[ ] Restaurant REST API implemented
[ ] Order REST API implemented
[ ] PostgreSQL 16.15 installed
[ ] food_delivery database created
[ ] Spring Data JPA added
[ ] PostgreSQL JDBC driver added
[ ] Restaurant entity created
[ ] Restaurant repository created
[ ] Restaurant API connected to PostgreSQL
[ ] Restaurant data persists in PostgreSQL
[ ] Order entity created
[ ] Order repository created
[ ] Order API connected to PostgreSQL
[ ] Order data persists in PostgreSQL
[ ] GET /restaurants tested in Postman
[ ] POST /restaurants tested in Postman
[ ] POST /orders tested in Postman
[ ] SOA components documented
[ ] SOA vs Microservices comparison prepared
```

------------------------------------------------------------------------

# 33. Quick Start for Someone Repeating the Experiment

If someone wants to reproduce the project from scratch:

``` text
1. Install Java 21.
2. Install PostgreSQL 16.
3. Create database: food_delivery.
4. Open Spring Tools.
5. Create restaurant-service.
6. Create order-service.
7. Add Spring Web, JPA, and PostgreSQL dependencies.
8. Configure PostgreSQL in application.properties.
9. Create Restaurant entity.
10. Create RestaurantRepository.
11. Create RestaurantController.
12. Create Order entity.
13. Create OrderRepository.
14. Create OrderController.
15. Start PostgreSQL.
16. Start Restaurant Service on 8081.
17. Start Order Service on 8082.
18. Test REST APIs using Postman.
19. Verify records using PostgreSQL.
20. Prepare SOA documentation and comparison.
```

------------------------------------------------------------------------

# 34. Key Learning

The most important concept from this experiment is the separation of
responsibilities:

``` text
Restaurant Service
    |
    +-- Restaurant Controller
    +-- Restaurant Entity
    +-- Restaurant Repository
    +-- PostgreSQL data


Order Service
    |
    +-- Order Controller
    +-- Order Entity
    +-- Order Repository
    +-- PostgreSQL data
```

The services expose clearly defined REST contracts while keeping their
implementation internally organized.

The major progression during this experiment was:

``` text
Hard-coded mock data
        |
        v
REST API
        |
        v
Spring Data JPA
        |
        v
PostgreSQL persistence
```

That progression demonstrates the core SOA programming concepts required
for the experiment.
