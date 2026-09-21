package com.stockdrive.controller;

import com.stockdrive.model.LoginRequest;
import com.stockdrive.model.LoginResponse;
import com.stockdrive.model.User;
import com.stockdrive.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // REGISTER
    // =========================

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        user.setPasswordHash(
                passwordEncoder.encode(user.getPasswordHash())
        );

        user.setRole(User.Role.STAFF);
        user.setStatus(User.Status.ACTIVE);

        User savedUser = userRepository.save(user);

        savedUser.setPasswordHash(null);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedUser);
    }

    // =========================
    // LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest) {

        User user = userRepository
                .findByUsername(loginRequest.getUsername())
                .orElse(null);

        if (user == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        if (user.getStatus() != User.Status.ACTIVE) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("User account is inactive");
        }

        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid username or password");
        }

        LoginResponse response = new LoginResponse(
                "Login successful",
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }
}