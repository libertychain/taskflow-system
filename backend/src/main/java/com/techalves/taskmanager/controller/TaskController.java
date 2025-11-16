package com.techalves.taskmanager.controller;

import com.techalves.taskmanager.dto.TaskDTO;
import com.techalves.taskmanager.service.AuthService;
import com.techalves.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "http://localhost:8081")
public class TaskController {
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private AuthService authService;
    
    @GetMapping
    public ResponseEntity<?> getAllTasks(@RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<TaskDTO> tasks = taskService.getAllTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar tarefas: " + e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(@PathVariable Long userId, 
                                          @RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<TaskDTO> tasks = taskService.getTasksByUser(userId);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar tarefas: " + e.getMessage()));
        }
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable String status, 
                                            @RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            List<TaskDTO> tasks = taskService.getTasksByStatus(status);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar tarefas: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id, 
                                       @RequestHeader(value = "Authorization", required = false) String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            Optional<TaskDTO> task = taskService.getTaskById(id);
            return task.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao buscar tarefa: " + e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody TaskDTO taskDTO, 
                                       @RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            String createdBy = getCurrentUserEmail(sessionId);
            TaskDTO createdTask = taskService.createTask(taskDTO, createdBy);
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao criar tarefa: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, 
                                       @RequestBody TaskDTO taskDTO, 
                                       @RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            Optional<TaskDTO> updatedTask = taskService.updateTask(id, taskDTO);
            return updatedTask.map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao atualizar tarefa: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id, 
                                       @RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            boolean deleted = taskService.deleteTask(id);
            if (deleted) {
                return ResponseEntity.ok(Map.of("message", "Tarefa excluída com sucesso"));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao excluir tarefa: " + e.getMessage()));
        }
    }
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long id, 
                                            @RequestBody Map<String, String> statusMap, 
                                            @RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            String status = statusMap.get("status");
            Optional<TaskDTO> updatedTask = taskService.updateTaskStatus(id, status);
            return updatedTask.map(ResponseEntity::ok)
                             .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Erro ao atualizar status: " + e.getMessage()));
        }
    }
    
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats(@RequestHeader("Authorization") String sessionId) {
        if (!isValidSession(sessionId)) {
            return ResponseEntity.status(401).body(Map.of("error", "Não autorizado"));
        }
        
        try {
            Map<String, Object> stats = taskService.getDashboardStats();
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