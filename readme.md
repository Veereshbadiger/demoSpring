```markdown
# API Usage

## Register User

To register a new user, use the following `curl` command:


```http://localhost:8080/auth/registerAdmin```
```{"name":"John Doe", "email":"john.doe@example.com", "password":"password123", "role":"Admin"}```

```http://localhost:8080/auth/loginAdmin```
```{"email": "john.doe@example.com","password": "password123"}```

## Spring Usages

### Register User Endpoint

In a Spring Boot application, you can create a registration endpoint as follows:

```java
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Logic to register user
        return ResponseEntity.ok("User registered successfully");
    }
}
```

### User Entity

Define a `User` entity to map the incoming JSON data:

```java
public class User {
    private String name;
    private String email;
    private String password;

    // Getters and Setters
}
```

### Application Properties

Ensure your application is configured to run on port 8080 by setting the following in `application.properties`:

```
server.port=8080
```
```