package com.techalves.taskmanager.service;

import com.techalves.taskmanager.dto.LoginDTO;
import com.techalves.taskmanager.model.User;
import com.techalves.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AuthService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Simulação de sessão (em produção, usar JWT ou Spring Security)
    private Map<String, User> activeSessions = new HashMap<>();
    
    public Map<String, Object> login(LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());
            
            if (userOptional.isEmpty()) {
                response.put("success", false);
                response.put("message", "Usuário não encontrado");
                return response;
            }
            
            User user = userOptional.get();
            
            // Verificar senha (em produção, usar BCrypt)
            if (!user.getPassword().equals(loginDTO.getPassword())) {
                response.put("success", false);
                response.put("message", "Senha incorreta");
                return response;
            }
            
            // Criar sessão
            String sessionId = generateSessionId();
            activeSessions.put(sessionId, user);
            
            response.put("success", true);
            response.put("message", "Login realizado com sucesso");
            response.put("sessionId", sessionId);
            response.put("user", user);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao realizar login: " + e.getMessage());
        }
        
        return response;
    }
    
    public Map<String, Object> logout(String sessionId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (activeSessions.containsKey(sessionId)) {
                activeSessions.remove(sessionId);
                response.put("success", true);
                response.put("message", "Logout realizado com sucesso");
            } else {
                response.put("success", false);
                response.put("message", "Sessão não encontrada");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao realizar logout: " + e.getMessage());
        }
        
        return response;
    }
    
    public User getUserFromSession(String sessionId) {
        return activeSessions.get(sessionId);
    }
    
    public boolean isValidSession(String sessionId) {
        return activeSessions.containsKey(sessionId);
    }
    
    public User getCurrentUser(String sessionId) {
        return activeSessions.get(sessionId);
    }
    
    private String generateSessionId() {
        return "session_" + System.currentTimeMillis() + "_" + Math.random();
    }
}