package com.example.demo.service;

import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin createAdmin(Admin Admin) {
        return adminRepository.save(Admin);
    }

    public Optional<Admin> updateAdmin(Long id, Admin newAdmin) {
        return adminRepository.findById(id).map(existingAdmin -> {
            existingAdmin.setName(newAdmin.getName());
            existingAdmin.setEmail(newAdmin.getEmail());
            return adminRepository.save(existingAdmin);
        });
    }

    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public boolean isAdmin(String email) {

        Optional<Admin> adminOptional = adminRepository.findByEmail(email);

        return adminOptional.isPresent() && adminOptional.get().getRole().equals("ADMIN");

    }
}
