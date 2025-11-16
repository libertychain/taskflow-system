package com.techalves.taskmanager.controller;

import com.techalves.taskmanager.model.User;
import com.techalves.taskmanager.repository.UserRepository;
import com.techalves.taskmanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8081")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthService authService;
    
    @GetMapping
    public ResponseEntity<?> getAllUsers(@RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar usuários: " + e.getMessage()));
        }
    }
    
    @GetMapping("/departments")
    public ResponseEntity<?> getDepartments(@RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<String> departments = userRepository.findAll()
                    .stream()
                    .map(User::getDepartment)
                    .distinct()
                    .toList();
            
            return ResponseEntity.ok(departments);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar departamentos: " + e.getMessage()));
        }
    }
    
    @GetMapping("/roles")
    public ResponseEntity<?> getRoles(@RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<String> roles = userRepository.findAll()
                    .stream()
                    .map(User::getRole)
                    .distinct()
                    .toList();
            
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar roles: " + e.getMessage()));
        }
    }
    
    private boolean isValidSession(String sessionId) {
        if (sessionId == null || !sessionId.startsWith("Bearer ")) {
            return false;
        }
        String token = sessionId.substring(7);
        return authService.isValidSession(token);
    }
}