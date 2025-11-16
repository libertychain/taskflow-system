package com.techalves.taskmanager.controller;

import com.techalves.taskmanager.dto.LoginDTO;
import com.techalves.taskmanager.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:8081")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = authService.login(loginDTO);
        
        if ((Boolean) response.get("success")) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body(response);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String sessionId) {
        if (sessionId != null && sessionId.startsWith("Bearer ")) {
            String token = sessionId.substring(7);
            Map<String, Object> response = authService.logout(token);
            
            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body(response);
            }
        }
        
        return ResponseEntity.status(400).body(Map.of("success", false, "message", "Sessão inválida"));
    }
    
    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String sessionId) {
        if (sessionId != null && sessionId.startsWith("Bearer ")) {
            String token = sessionId.substring(7);
            
            if (authService.isValidSession(token)) {
                return ResponseEntity.ok(authService.getCurrentUser(token));
            }
        }
        
        return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
    }
    
    @GetMapping("/validate")
    public ResponseEntity<?> validateSession(@RequestHeader("Authorization") String sessionId) {
        if (sessionId != null && sessionId.startsWith("Bearer ")) {
            String token = sessionId.substring(7);
            boolean isValid = authService.isValidSession(token);
            
            return ResponseEntity.ok(Map.of("valid", isValid));
        }
        
        return ResponseEntity.ok(Map.of("valid", false));
    }
}