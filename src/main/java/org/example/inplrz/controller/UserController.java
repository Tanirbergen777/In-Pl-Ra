package org.example.inplrz.controller;

import org.example.inplrz.entity.User;
import org.example.inplrz.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/manager/{managerId}/subordinates")
    public ResponseEntity<List<User>> getSubordinates(@PathVariable UUID managerId) {
        List<User> subordinates = userRepository.findByManagerId(managerId);
        return ResponseEntity.ok(subordinates);
    }
}