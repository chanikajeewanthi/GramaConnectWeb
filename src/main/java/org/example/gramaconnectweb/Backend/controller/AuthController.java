package org.example.gramaconnectweb.Backend.controller;

import org.example.gramaconnectweb.Backend.entity.Role;
import org.example.gramaconnectweb.Backend.entity.User;
import org.example.gramaconnectweb.Backend.service.JwtUtil;
import org.example.gramaconnectweb.Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ Register user with role from DB
//    @PostMapping("/register")
//    public ResponseEntity<?> register(
//            @RequestBody User user,
//            @RequestParam(required = false) String role) {
//
//        // If no role provided → default VILLAGER
//        String roleName = (role != null) ? "ROLE_" + role.toUpperCase() : "ROLE_VILLAGER";
//
//        try {
//            User savedUser = userService.saveUser(user, Set.of(roleName));
//
//            return ResponseEntity.ok(Map.of(
//                    "message", "User registered successfully",
//                    "username", savedUser.getUsername(),
//                    "email", savedUser.getEmail(),
//                    "roles", savedUser.getRoles().stream().map(r -> r.getName()).toList()
//            ));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", e.getMessage()));
//        }
//    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> request) {
        try {
            String username = (String) request.get("username");
            String email = (String) request.get("email");
            String password = (String) request.get("password");

            @SuppressWarnings("unchecked")
            List<String> rolesFromRequest = (List<String>) request.get("roles");

            // Default to VILLAGER if no roles provided
            Set<String> roleNames = (rolesFromRequest == null || rolesFromRequest.isEmpty())
                    ? Set.of("ROLE_VILLAGER")
                    : rolesFromRequest.stream()
                    .map(r -> "ROLE_" + r.toUpperCase())
                    .collect(java.util.stream.Collectors.toSet());

            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(password);

            User savedUser = userService.saveUser(user, roleNames);

            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "username", savedUser.getUsername(),
                    "email", savedUser.getEmail(),
                    "roles", savedUser.getRoles().stream().map(Role::getName).toList()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ Login with email + password
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            User dbUser = userService.findByEmail(user.getEmail());

            List<String> roleNames = dbUser.getRoles().stream()
                    .map(role -> role.getName())
                    .toList();

            String token = jwtUtil.generateToken(dbUser.getEmail(), roleNames);

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "roles", roleNames,
                    "email", dbUser.getEmail()
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password!"));
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        User user = userService.getCurrentUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Not logged in"));
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail(),
                "roles", user.getRoles().stream().map(Role::getName).toList()
        ));
    }

}
