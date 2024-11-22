// File: src/main/java/com/example/demo/controller/AuthenticationController.java
package com.example.demo.controller;

import com.example.demo.model.Admin;
import com.example.demo.model.User;
import com.example.demo.repository.AdminRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/registerUser")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Hash the password
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<Admin> registerAdmin(@RequestBody Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword())); // Hash the password
        Admin savedAdmin = adminRepository.save(admin);
        return ResponseEntity.ok(savedAdmin);
    }

    @PostMapping("/loginUser")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        try {
            // Validate input
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.status(400).body("Email and password are required");
            }

            // Find user by email
            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            User user = userOptional.get();

            // Check password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }


    @PostMapping("/loginAdmin")
    public ResponseEntity<?> loginUser(@RequestBody Admin loginRequest) {
        try {
            // Validate input
            if (loginRequest.getEmail() == null || loginRequest.getPassword() == null) {
                return ResponseEntity.status(400).body("Email and password are required");
            }

            // Find user by email
            Optional<Admin> adminOptional = adminRepository.findByEmail(loginRequest.getEmail());
            if (adminOptional.isEmpty()) {
                return ResponseEntity.status(404).body("User not found");
            }

            Admin admin = adminOptional.get();

            // Check password
            if (!passwordEncoder.matches(loginRequest.getPassword(), admin.getPassword())) {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(admin.getEmail(), admin.getRole());
            return ResponseEntity.ok(token);

        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

}
