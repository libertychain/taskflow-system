package com.techalves.taskmanager.controller;

import com.techalves.taskmanager.dto.ProjectDTO;
import com.techalves.taskmanager.service.AuthService;
import com.techalves.taskmanager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:8081")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private AuthService authService;
    
    @GetMapping
    public ResponseEntity<?> getAllProjects(@RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<ProjectDTO> projects = projectService.getAllProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar projetos: " + e.getMessage()));
        }
    }
    
    @GetMapping("/active")
    public ResponseEntity<?> getActiveProjects(@RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<ProjectDTO> projects = projectService.getActiveProjects();
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar projetos ativos: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id, 
                                          @RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            Optional<ProjectDTO> project = projectService.getProjectById(id);
            return project.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar projeto: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectDTO projectDTO, 
                                         @RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            String createdBy = getCurrentUserEmail(sessionId);
            ProjectDTO createdProject = projectService.createProject(projectDTO, createdBy);
            return ResponseEntity.ok(createdProject);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar projeto: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, 
                                         @RequestBody ProjectDTO projectDTO, 
                                         @RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            Optional<ProjectDTO> updatedProject = projectService.updateProject(id, projectDTO);
            return updatedProject.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao atualizar projeto: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProject(@PathVariable Long id, 
                                         @RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            boolean deleted = projectService.deleteProject(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Projeto excluído com sucesso"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao excluir projeto: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateProjectStatus(@PathVariable Long id, 
                                               @RequestBody Map<String, String> statusMap, 
                                               @RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            String status = statusMap.get("status");
            Optional<ProjectDTO> updatedProject = projectService.updateProjectStatus(id, status);
            return updatedProject.map(ResponseEntity::ok)
                                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao atualizar status: " + e.getMessage()));
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<?> getProjectStats(@RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<Map<String, Object>> stats = projectService.getProjectsWithStats();
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar estatísticas: " + e.getMessage()));
        }
    }
    
    private boolean isValidSession(String sessionId) {
        if (sessionId == null || !sessionId.startsWith("Bearer ")) {
            return false;
        }
        String token = sessionId.substring(7);
        return authService.isValidSession(token);
    }
    
    private String getCurrentUserEmail(String sessionId) {
        String token = sessionId.substring(7);
        return authService.getCurrentUser(token).getEmail();
    }
}