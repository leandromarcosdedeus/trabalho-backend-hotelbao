package com.hotelbao.controller;

import com.hotelbao.entity.Role;
import com.hotelbao.entity.User;
import com.hotelbao.repository.RoleRepository;
import com.hotelbao.repository.UserRepository;
import com.hotelbao.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username já cadastrado");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        // Definir role
        final Role.RoleType roleType = (user.getRole() != null) ? user.getRole().getName() : Role.RoleType.ROLE_USER;
        Role role = roleRepository.findByName(roleType)
            .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleType));
        user.setRole(role);
        userRepository.save(user);
        return ResponseEntity.ok().body(Collections.singletonMap("message", "Usuário cadastrado com sucesso!"));
    }

    @PostMapping("/signin")
    public ResponseEntity<Map<String, Object>> authenticateUser(@RequestBody User login) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userRepository.findByUsername(login.getUsername()).orElse(null);
        String jwt = jwtUtils.generateJwtToken(authentication, user != null ? user.getId() : null);
        
        String role = user != null && user.getRole() != null ? user.getRole().getName().name() : null;
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("token", jwt);
        response.put("role", role);
        response.put("userId", user != null ? user.getId() : null);
        response.put("userName", user != null ? user.getName() : null);
        
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/users")
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            String role = user.getRole() != null ? user.getRole().getName().name() : null;
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", user.getId());
            map.put("name", user.getName());
            map.put("email", user.getEmail());
            map.put("username", user.getUsername());
            map.put("phone", user.getPhone());
            map.put("address", user.getAddress());
            map.put("city", user.getCity());
            map.put("roleName", role);
            return map;
        }).collect(Collectors.toList());
    }

    @GetMapping("/users-roles")
    public List<Map<String, Object>> getAllUsersWithRoles() {
        return userRepository.findAll().stream().map(user -> {
            String roleName = user.getRole() != null ? user.getRole().getName().name() : null;
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("name", user.getName());
            map.put("email", user.getEmail());
            map.put("username", user.getUsername());
            map.put("phone", user.getPhone());
            map.put("roleName", roleName);
            return map;
        }).collect(Collectors.toList());
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userUpdate) {
        return userRepository.findById(id).map(user -> {
            user.setName(userUpdate.getName());
            user.setEmail(userUpdate.getEmail());
            user.setUsername(userUpdate.getUsername());
            user.setPhone(userUpdate.getPhone());
            user.setAddress(userUpdate.getAddress());
            user.setCity(userUpdate.getCity());
            if (userUpdate.getPassword() != null && !userUpdate.getPassword().isEmpty()) {
                user.setPassword(encoder.encode(userUpdate.getPassword()));
            }
            if (userUpdate.getRole() != null) {
                Role.RoleType roleType = userUpdate.getRole().getName();
                Role role = roleRepository.findByName(roleType)
                    .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleType));
                user.setRole(role);
            }
            userRepository.save(user);
            return ResponseEntity.ok(java.util.Collections.singletonMap("message", "Usuário atualizado com sucesso!"));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().body(Collections.singletonMap("message", "Usuário deletado com sucesso!"));
    }
} 