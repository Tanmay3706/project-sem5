package com.stockdrive.controller;

import com.stockdrive.model.LoginRequest;
import com.stockdrive.model.LoginResponse;
import com.stockdrive.model.User;
import com.stockdrive.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins="*")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.existsByUsername(user.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");

        if (userRepository.existsByEmail(user.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRole(User.Role.STAFF);
        user.setStatus(User.Status.ACTIVE);

        User savedUser = userRepository.save(user);
        savedUser.setPasswordHash(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername()).orElse(null);

        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

        if (user.getStatus() != User.Status.ACTIVE)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User account is inactive");

        boolean passwordMatches = passwordEncoder.matches(
                loginRequest.getPassword(),
                user.getPasswordHash()
        );

        if (!passwordMatches)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");

        LoginResponse response = new LoginResponse(
                "Login successful",
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        List<User> users = userRepository.findAll();
        users.forEach(user -> user.setPasswordHash(null));
        return ResponseEntity.ok(users);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setPasswordHash(null);
                    return ResponseEntity.ok(user);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/users/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam User.Status status) {

        return userRepository.findById(id)
                .map(user -> {
                    user.setStatus(status);
                    User saved = userRepository.save(user);
                    saved.setPasswordHash(null);
                    return ResponseEntity.ok(saved);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/users")
    public ResponseEntity<?> createUser(@RequestBody User user) {

        if (user.getUsername() == null || user.getUsername().trim().isEmpty())
            return ResponseEntity.badRequest().body("Username is required");

        if (user.getEmail() == null || user.getEmail().trim().isEmpty())
            return ResponseEntity.badRequest().body("Email is required");

        if (user.getPasswordHash() == null || user.getPasswordHash().trim().isEmpty())
            return ResponseEntity.badRequest().body("Password is required");

        if (userRepository.existsByUsername(user.getUsername()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");

        if (userRepository.existsByEmail(user.getEmail()))
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        if (user.getRole() == null)
            user.setRole(User.Role.STAFF);

        if (user.getStatus() == null)
            user.setStatus(User.Status.ACTIVE);

        User savedUser = userRepository.save(user);
        savedUser.setPasswordHash(null);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}