# SOA Skill Experiment 3 --- User Registration System with Validation

## Course Information

-   **Course:** SOA Programming and Microservices
-   **Academic Year:** 2026--27
-   **Course Code:** 24SDCS03 A/R
-   **Experiment:** 3
-   **Experiment Title:** User Registration System with Validation
-   **Java:** Java 21
-   **Spring Boot:** 4.1.1
-   **Database:** PostgreSQL 16
-   **Database access:** PostgreSQL CLI (`psql`)
-   **Base package:** `com.klu.userregistration`

------------------------------------------------------------------------

# 1. Experiment Objective

The goal of this experiment was to build a backend user registration and
login system for a job portal.

The system needed to prevent:

-   Incorrect user data
-   Duplicate usernames and emails
-   Invalid email addresses
-   Missing required fields
-   Poor error messages
-   Insecure password storage

The final application provides:

-   `POST /register`
-   `POST /login`
-   Validation using Jakarta Bean Validation
-   PostgreSQL persistence using Spring Data JPA
-   Unique database constraints
-   BCrypt password hashing
-   Validation and error handling
-   Login credential verification

------------------------------------------------------------------------

# 2. Requirements Given in the Experiment

## Step 1 --- Design User Entity

The entity must contain:

-   Username --- unique
-   Email --- unique and valid
-   Password

## Step 2 --- Registration API

Endpoint:

``` text
POST /register
```

The API must store the user in PostgreSQL.

## Step 3 --- Login API

Endpoint:

``` text
POST /login
```

The API must validate the supplied credentials.

## Step 4 --- Validation Rules

The application must validate:

-   Email format
-   Non-empty fields
-   Unique username
-   Unique email

## Step 5 --- Error Handling

The application must handle:

-   Duplicate user
-   Invalid input
-   Missing fields

## Step 6 --- Test Scenarios

We tested:

-   Valid registration
-   Duplicate registration
-   Invalid email

We also tested successful and unsuccessful login.

------------------------------------------------------------------------

# 3. Technologies Used

``` text
Java 21
Spring Boot 4.1.1
Spring Web
Spring Data JPA
Hibernate
PostgreSQL 16
PostgreSQL JDBC Driver
Jakarta Validation
BCrypt
Maven
Spring Tools for Eclipse
cURL
```

------------------------------------------------------------------------

# 4. Final Project Architecture

The application follows a simple layered Spring Boot architecture.

``` text
                    Client
                      |
                      | HTTP / JSON
                      v
              +----------------+
              | UserController |
              +----------------+
                 |          |
                 |          |
          /register       /login
                 |          |
                 v          v
              +----------------+
              |   UserService  |
              +----------------+
                 |          |
                 |          |
          validation      BCrypt
                 |       password check
                 v          |
              +----------------+
              | UserRepository |
              +----------------+
                      |
                      | JPA / Hibernate
                      v
              +----------------+
              |   PostgreSQL   |
              | user_registration_db
              +----------------+
```

Supporting components:

``` text
entity/
    User.java

repository/
    UserRepository.java

service/
    UserService.java

controller/
    UserController.java

exception/
    GlobalExceptionHandler.java

config/
    SecurityConfig.java
```

------------------------------------------------------------------------

# 5. Final Project Structure

The project structure is:

``` text
user-registration/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── klu/
│   │   │           └── userregistration/
│   │   │               │
│   │   │               ├── UserRegistrationApplication.java
│   │   │               │
│   │   │               ├── config/
│   │   │               │   └── SecurityConfig.java
│   │   │               │
│   │   │               ├── controller/
│   │   │               │   └── UserController.java
│   │   │               │
│   │   │               ├── entity/
│   │   │               │   └── User.java
│   │   │               │
│   │   │               ├── exception/
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               │
│   │   │               ├── repository/
│   │   │               │   └── UserRepository.java
│   │   │               │
│   │   │               └── service/
│   │   │                   └── UserService.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│
├── pom.xml
└── README.md
```

------------------------------------------------------------------------

# 6. Database Setup

We used PostgreSQL through the command-line interface instead of
pgAdmin.

First, PostgreSQL was opened using `psql`.

The database was created using:

``` sql
CREATE DATABASE user_registration_db;
```

Then we connected to it:

``` text
\c user_registration_db
```

We checked the available tables:

``` text
\dt
```

Initially there were no relations.

After the Spring Boot application started with JPA configured, Hibernate
created the `users` table.

We verified it with:

``` text
\dt
```

Then:

``` text
\d users
```

The final table contains:

``` text
id
email
password
username
```

The database also contains:

-   Primary key on `id`
-   Unique constraint on `email`
-   Unique constraint on `username`

------------------------------------------------------------------------

# 7. User Entity

The `User` entity represents a user stored in PostgreSQL.

Important validation rules were added to the fields.

Conceptually:

``` java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Column(unique = true, nullable = false)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;
}
```

The exact final source code should be kept in the project itself.

------------------------------------------------------------------------

# 8. Repository Layer

Spring Data JPA was used to communicate with PostgreSQL.

The repository extends:

``` java
JpaRepository<User, Long>
```

It contains methods for checking duplicates:

``` java
boolean existsByUsername(String username);

boolean existsByEmail(String email);
```

For login, we added:

``` java
Optional<User> findByUsername(String username);
```

Spring Data JPA automatically creates the appropriate database queries
from these method names.

------------------------------------------------------------------------

# 9. Registration Flow

The registration request is sent to:

``` text
POST /register
```

Example:

``` bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"rahul\",\"email\":\"rahul@gmail.com\",\"password\":\"hello123\"}"
```

The flow is:

``` text
Client
  |
  v
POST /register
  |
  v
UserController
  |
  | @Valid
  v
Validation
  |
  +---- invalid ----> GlobalExceptionHandler
  |
  v
UserService
  |
  +---- duplicate username ----> error
  |
  +---- duplicate email --------> error
  |
  v
BCrypt password hashing
  |
  v
UserRepository
  |
  v
PostgreSQL
```

A valid registration returned:

``` text
User registered successfully
```

------------------------------------------------------------------------

# 10. Validation

We tested all major validation rules.

## Invalid email

Request:

``` json
{
    "username": "testuser",
    "email": "not-an-email",
    "password": "hello123"
}
```

Result:

``` text
Invalid email format
```

The invalid user was not inserted into PostgreSQL.

## Empty username

Result:

``` text
Username is required
```

## Empty email

Result:

``` text
Email is required
```

## Empty password

Result:

``` text
Password is required
```

This demonstrated backend validation and prevented invalid data from
reaching the database.

------------------------------------------------------------------------

# 11. Global Exception Handler

A `GlobalExceptionHandler` was created using:

``` java
@RestControllerAdvice
```

It handles:

``` java
MethodArgumentNotValidException
```

and extracts the validation message.

Without this handler, Spring returned a generic response such as:

``` json
{
    "status": 400,
    "error": "Bad Request",
    "path": "/register"
}
```

After implementing the handler, the response became much more useful:

``` text
Invalid email format
```

This improved the API's error handling and user experience.

------------------------------------------------------------------------

# 12. Duplicate Registration

We tested duplicate username and duplicate email separately.

## Duplicate username

A request using the existing username returned:

``` text
Username already exists
```

## Duplicate email

A request using the existing email returned:

``` text
Email already exists
```

This demonstrated both application-level checking and database-level
unique constraints.

------------------------------------------------------------------------

# 13. Password Security with BCrypt

Initially, passwords were stored directly.

For example:

``` text
hello123
```

appeared directly in PostgreSQL.

This was not secure.

We corrected this by adding the Spring Security Crypto dependency and
creating a `PasswordEncoder` bean using:

``` java
new BCryptPasswordEncoder()
```

The registration service now performs:

``` java
String encodedPassword =
        passwordEncoder.encode(user.getPassword());

user.setPassword(encodedPassword);
```

Therefore, a password such as:

``` text
hello123
```

is stored as a BCrypt hash similar to:

``` text
$2a$10$...
```

The exact hash changes because BCrypt uses a salt.

------------------------------------------------------------------------

# 14. Important Database Security Issue We Encountered

The first test user was created before BCrypt was implemented.

Therefore the database temporarily contained:

``` text
samarth -> hello123
```

while a newly created user contained a BCrypt hash.

This was not a failure of BCrypt. It happened because the old record was
created before the security improvement.

For a clean final demonstration, old test data can be deleted and
recreated after BCrypt is enabled.

Example:

``` sql
DELETE FROM users;
```

Then:

``` sql
SELECT * FROM users;
```

The new users should show BCrypt hashes rather than plaintext passwords.

------------------------------------------------------------------------

# 15. Login API

The login endpoint is:

``` text
POST /login
```

The current implementation accepts username and password as request
parameters.

Example:

``` bash
curl -X POST "http://localhost:8080/login?username=rahul&password=hello123"
```

Successful result:

``` text
Login successful
```

------------------------------------------------------------------------

# 16. Login Authentication Flow

The login process is:

``` text
Client
  |
  v
POST /login
  |
  v
UserController
  |
  v
UserService
  |
  v
findByUsername()
  |
  +---- user not found ----> Invalid username or password
  |
  v
BCrypt.matches()
  |
  +---- password incorrect ----> Invalid username or password
  |
  v
Login successful
```

The application does not compare:

``` java
password.equals(storedPassword)
```

because the database stores a hash.

Instead it uses:

``` java
passwordEncoder.matches(
    password,
    user.getPassword()
);
```

------------------------------------------------------------------------

# 17. Login Testing

## Correct credentials

``` bash
curl -X POST "http://localhost:8080/login?username=rahul&password=hello123"
```

Result:

``` text
Login successful
```

## Wrong password

``` bash
curl -X POST "http://localhost:8080/login?username=rahul&password=wrongpassword"
```

Result:

``` text
Invalid username or password
```

The same error message is used for an unknown username and wrong
password. This avoids unnecessarily revealing whether a particular
username exists.

------------------------------------------------------------------------

# 18. Challenges We Faced and How We Solved Them

## Challenge 1 --- PostgreSQL database initially had no tables

When we first ran:

``` text
\dt
```

PostgreSQL showed no relations.

### Cause

The database had been created, but the application had not yet created
the JPA entity table.

### Solution

We configured the Spring Boot application to connect to:

``` text
jdbc:postgresql://localhost:5432/user_registration_db
```

and allowed Hibernate/JPA to create the `users` table.

After starting the application,:

``` text
\dt
```

showed:

``` text
public | users | table | postgres
```

------------------------------------------------------------------------

## Challenge 2 --- We were unsure whether PostgreSQL was actually connected

The Spring Boot console showed:

``` text
HikariPool - Added connection
```

and:

``` text
Database JDBC URL:
jdbc:postgresql://localhost:5432/user_registration_db
```

### Solution

We verified the database directly using `psql`.

We used:

``` text
\c user_registration_db
```

``` text
\dt
```

``` text
\d users
```

``` sql
SELECT * FROM users;
```

This confirmed that Spring Boot was successfully communicating with
PostgreSQL.

------------------------------------------------------------------------

## Challenge 3 --- Invalid email initially returned a generic 400 response

The first invalid-email test returned:

``` json
{
    "status": 400,
    "error": "Bad Request",
    "path": "/register"
}
```

### Cause

Validation was working, but there was no custom exception handler.

### Solution

We created:

``` text
exception/GlobalExceptionHandler.java
```

using:

``` java
@RestControllerAdvice
```

After that, the same test returned:

``` text
Invalid email format
```

------------------------------------------------------------------------

## Challenge 4 --- Windows Command Prompt typo

At one point we typed:

``` text
url -X POST ...
```

instead of:

``` text
curl -X POST ...
```

Windows reported:

``` text
'url' is not recognized as an internal or external command
```

### Solution

We corrected the command to:

``` text
curl -X POST ...
```

This was a command-line typing error, not an application problem.

------------------------------------------------------------------------

## Challenge 5 --- Server was not running

One registration request returned:

``` text
curl: (7) Failed to connect to localhost:8080
```

### Cause

Spring Boot was not running at that moment.

### Solution

We started the project again using:

``` text
Run As → Spring Boot App
```

and waited for:

``` text
Started UserRegistrationApplication
```

After that, requests worked again.

------------------------------------------------------------------------

## Challenge 6 --- `/login` returned 404

The first login request returned:

``` json
{
    "status": 404,
    "error": "Not Found",
    "path": "/login"
}
```

### Cause

The `UserController` contained `/register`, but we had not yet added:

``` java
@PostMapping("/login")
```

### Solution

We added the login endpoint to `UserController`.

------------------------------------------------------------------------

## Challenge 7 --- `loginUser()` was undefined

After adding `/login` to the controller, Spring Tools displayed:

``` text
The method loginUser(String, String)
is undefined for the type UserService
```

### Cause

The controller was calling:

``` java
userService.loginUser(username, password);
```

but the service did not yet have that method.

### Solution

We added:

``` java
public String loginUser(String username, String password)
```

to `UserService`.

The method finds the user and verifies the password with BCrypt.

------------------------------------------------------------------------

## Challenge 8 --- Passwords were initially stored as plaintext

PostgreSQL initially showed:

``` text
hello123
```

directly in the password column.

### Cause

The registration service was saving the entity without hashing the
password.

### Solution

We added BCrypt and changed the service to encode the password before
saving.

After that, PostgreSQL contained values similar to:

``` text
$2a$10$...
```

This solved the secure password storage requirement.

------------------------------------------------------------------------

# 19. Why We Did Not Add Extra Features

This experiment does not require a complete production authentication
platform.

Therefore we intentionally did not add:

-   JWT
-   Role-based access control
-   Admin accounts
-   Password reset
-   Email verification
-   Frontend
-   Swagger
-   Docker
-   Full Spring Security authentication configuration
-   Microservice decomposition

These features would go beyond the stated academic experiment.

The implementation focuses on the exact learning objectives:

-   Backend validation
-   Database constraints
-   Secure password storage
-   API error handling
-   Basic authentication

------------------------------------------------------------------------

# 20. How to Run the Project

## Prerequisites

Install:

``` text
Java 21
PostgreSQL 16
Maven
Spring Tools for Eclipse / Eclipse
```

Make sure PostgreSQL is running.

------------------------------------------------------------------------

# 21. Create the Database

Open PostgreSQL CLI:

``` bash
psql -U postgres
```

Create the database:

``` sql
CREATE DATABASE user_registration_db;
```

Connect to it:

``` text
\c user_registration_db
```

Check:

``` text
\dt
```

The `users` table will be created automatically by JPA when the
application starts, assuming the project is configured to create/update
the schema.

------------------------------------------------------------------------

# 22. Check `application.properties`

The application should contain PostgreSQL configuration similar to:

``` properties
spring.datasource.url=jdbc:postgresql://localhost:5432/user_registration_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

Replace:

``` text
YOUR_POSTGRES_PASSWORD
```

with your local PostgreSQL password.

Do not commit real database passwords to a public GitHub repository.

------------------------------------------------------------------------

# 23. Start the Application

In Spring Tools:

``` text
Right-click project
        ↓
Run As
        ↓
Spring Boot App
```

Wait for:

``` text
Tomcat started on port 8080
```

and:

``` text
Started UserRegistrationApplication
```

The API is now available at:

``` text
http://localhost:8080
```

------------------------------------------------------------------------

# 24. Test Registration

## Valid registration

``` bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"rahul\",\"email\":\"rahul@gmail.com\",\"password\":\"hello123\"}"
```

Expected:

``` text
User registered successfully
```

------------------------------------------------------------------------

# 25. Test Invalid Email

``` bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"email\":\"not-an-email\",\"password\":\"hello123\"}"
```

Expected:

``` text
Invalid email format
```

------------------------------------------------------------------------

# 26. Test Missing Fields

Empty username:

``` bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"\",\"email\":\"test@gmail.com\",\"password\":\"hello123\"}"
```

Expected:

``` text
Username is required
```

Empty email:

``` bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"email\":\"\",\"password\":\"hello123\"}"
```

Expected:

``` text
Email is required
```

Empty password:

``` bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"testuser\",\"email\":\"test2@gmail.com\",\"password\":\"\"}"
```

Expected:

``` text
Password is required
```

------------------------------------------------------------------------

# 27. Test Duplicate Registration

Duplicate username:

``` bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"samarth\",\"email\":\"another@gmail.com\",\"password\":\"hello123\"}"
```

Expected:

``` text
Username already exists
```

Duplicate email:

``` bash
curl -X POST http://localhost:8080/register -H "Content-Type: application/json" -d "{\"username\":\"anotheruser\",\"email\":\"samarth@gmail.com\",\"password\":\"hello123\"}"
```

Expected:

``` text
Email already exists
```

------------------------------------------------------------------------

# 28. Test Login

Successful login:

``` bash
curl -X POST "http://localhost:8080/login?username=rahul&password=hello123"
```

Expected:

``` text
Login successful
```

Wrong password:

``` bash
curl -X POST "http://localhost:8080/login?username=rahul&password=wrongpassword"
```

Expected:

``` text
Invalid username or password
```

Unknown username:

``` bash
curl -X POST "http://localhost:8080/login?username=unknown&password=hello123"
```

Expected:

``` text
Invalid username or password
```

------------------------------------------------------------------------

# 29. Verify Data in PostgreSQL

Connect to the database:

``` text
psql -U postgres
```

Then:

``` text
\c user_registration_db
```

Check tables:

``` text
\dt
```

Check table structure:

``` text
\d users
```

Check stored users:

``` sql
SELECT * FROM users;
```

For users created after BCrypt was implemented, the password column
should contain a BCrypt hash beginning with something similar to:

``` text
$2a$10$
```

It should not contain the original plaintext password.

------------------------------------------------------------------------

# 30. Complete Experiment Flow

The complete implementation can be remembered as:

``` text
1. Create Spring Boot project
          ↓
2. Configure PostgreSQL
          ↓
3. Create User entity
          ↓
4. Add JPA repository
          ↓
5. Create registration service
          ↓
6. Create /register API
          ↓
7. Add @NotBlank validation
          ↓
8. Add @Email validation
          ↓
9. Add unique username/email constraints
          ↓
10. Add GlobalExceptionHandler
          ↓
11. Test invalid input
          ↓
12. Add BCrypt password hashing
          ↓
13. Add /login API
          ↓
14. Add BCrypt credential verification
          ↓
15. Test successful login
          ↓
16. Test failed login
          ↓
17. Verify data in PostgreSQL
```

------------------------------------------------------------------------

# 31. Final Learning Outcomes

After completing this experiment, the following concepts were practiced:

## Backend Validation

Used:

``` text
@NotBlank
@Email
@Valid
```

to prevent invalid input.

## Database Constraints

Used:

``` text
PRIMARY KEY
UNIQUE(username)
UNIQUE(email)
NOT NULL
```

to maintain data consistency.

## Secure Data Storage

Used BCrypt to avoid storing passwords in plaintext.

## API Error Handling

Created a global exception handler for validation errors.

## Authentication Basics

Implemented login by:

1.  Finding the user by username.
2.  Retrieving the stored password hash.
3.  Comparing the supplied password using BCrypt.
4.  Returning success or an invalid-credentials response.

## Spring Boot Layered Architecture

Practiced separation into:

``` text
Controller
    ↓
Service
    ↓
Repository
    ↓
Database
```

------------------------------------------------------------------------

# 32. Experiment Completion Checklist

``` text
[✓] User entity created
[✓] Username field implemented
[✓] Email field implemented
[✓] Password field implemented

[✓] Username uniqueness
[✓] Email uniqueness
[✓] Email format validation
[✓] Non-empty validation

[✓] POST /register
[✓] PostgreSQL persistence

[✓] POST /login
[✓] Credential validation

[✓] Duplicate username handling
[✓] Duplicate email handling
[✓] Invalid email handling
[✓] Missing field handling
[✓] Invalid login handling

[✓] BCrypt password hashing
[✓] PostgreSQL verification

[✓] Valid registration tested
[✓] Duplicate registration tested
[✓] Invalid email tested
[✓] Successful login tested
[✓] Failed login tested

[✓] Experiment 3 completed
```

------------------------------------------------------------------------

# 33. Final Architecture Summary

The completed system is a Spring Boot REST application connected to
PostgreSQL.

``` text
                    +------------------+
                    |   cURL / Client  |
                    +--------+---------+
                             |
                             | HTTP
                             v
                  +----------------------+
                  |   UserController     |
                  |                      |
                  | POST /register       |
                  | POST /login          |
                  +----------+-----------+
                             |
                             v
                  +----------------------+
                  |     UserService      |
                  |                      |
                  | Registration logic    |
                  | Duplicate checking    |
                  | BCrypt hashing        |
                  | Login verification   |
                  +----------+-----------+
                             |
                             v
                  +----------------------+
                  |   UserRepository     |
                  |   Spring Data JPA    |
                  +----------+-----------+
                             |
                             v
                  +----------------------+
                  |     PostgreSQL       |
                  | user_registration_db |
                  |                      |
                  |       users          |
                  +----------------------+

Additional components:

GlobalExceptionHandler
        ↓
Validation error responses

SecurityConfig
        ↓
BCrypt PasswordEncoder
```

This is the final architecture for **SOA Skill Experiment 3 --- User
Registration System with Validation**.
