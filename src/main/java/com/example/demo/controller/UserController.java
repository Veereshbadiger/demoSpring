// File: src/main/java/com/example/demo/controller/UserController.java
package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.AdminService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;


@GetMapping
public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String token) {
    String email = jwtUtil.extractEmail(token.substring(7)); // Validate token
    if (!adminService.isAdmin(email)) {
        return ResponseEntity.status(403).build(); // Forbidden
    }
    return ResponseEntity.ok(userService.getAllUsers());
}


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestHeader("Authorization") String token) {
        if (!jwtUtil.hasRole(token.substring(7), "ADMIN")) {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
        return ResponseEntity.ok(userService.createUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!jwtUtil.hasRole(token.substring(7), "ADMIN")) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
