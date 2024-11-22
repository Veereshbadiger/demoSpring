package com.example.demo.controller;

import com.example.demo.model.Admin;
import com.example.demo.service.AdminService;
import com.example.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;


@GetMapping
public ResponseEntity<List<Admin>> getAllAdmins(@RequestHeader("Authorization") String token) {
    String email = jwtUtil.extractEmail(token.substring(7)); // Validate token
    if (!adminService.isAdmin(email)) {
        return ResponseEntity.status(403).build(); // Forbidden
    }
    return ResponseEntity.ok(adminService.getAllAdmins());
}


    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin Admin, @RequestHeader("Authorization") String token) {
        if (!jwtUtil.hasRole(token.substring(7), "ADMIN")) {
            return ResponseEntity.status(403).body(null); // Forbidden
        }
        return ResponseEntity.ok(adminService.createAdmin(Admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        if (!jwtUtil.hasRole(token.substring(7), "ADMIN")) {
            return ResponseEntity.status(403).build(); // Forbidden
        }
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
