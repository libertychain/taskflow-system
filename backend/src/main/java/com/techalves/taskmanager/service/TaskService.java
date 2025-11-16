package com.techalves.taskmanager.service;

import com.techalves.taskmanager.dto.TaskDTO;
import com.techalves.taskmanager.model.Task;
import com.techalves.taskmanager.model.User;
import com.techalves.taskmanager.repository.TaskRepository;
import com.techalves.taskmanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return convertToDTOList(tasks);
    }
    
    public List<TaskDTO> getTasksByUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            List<Task> tasks = taskRepository.findByAssignedTo(user.get());
            return convertToDTOList(tasks);
        }
        return new ArrayList<>();
    }
    
    public List<TaskDTO> getTasksByStatus(String status) {
        try {
            Task.TaskStatus taskStatus = Task.TaskStatus.valueOf(status.toUpperCase());
            List<Task> tasks = taskRepository.findByStatus(taskStatus);
            return convertToDTOList(tasks);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }
    
    public Optional<TaskDTO> getTaskById(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        return task.map(this::convertToDTO);
    }
    
    public TaskDTO createTask(TaskDTO taskDTO, String createdBy) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(Task.TaskStatus.PENDING);
        task.setPriority(taskDTO.getPriority() != null ? taskDTO.getPriority() : Task.TaskPriority.MEDIUM);
        task.setDueDate(taskDTO.getDueDate());
        task.setCreatedBy(createdBy);
        
        if (taskDTO.getAssignedToId() != null) {
            Optional<User> assignedUser = userRepository.findById(taskDTO.getAssignedToId());
            assignedUser.ifPresent(task::setAssignedTo);
        }
        
        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }
    
    public Optional<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
        Optional<Task> existingTask = taskRepository.findById(id);
        
        if (existingTask.isPresent()) {
            Task task = existingTask.get();
            task.setTitle(taskDTO.getTitle());
            task.setDescription(taskDTO.getDescription());
            task.setPriority(taskDTO.getPriority());
            task.setDueDate(taskDTO.getDueDate());
            
            if (taskDTO.getAssignedToId() != null) {
                Optional<User> assignedUser = userRepository.findById(taskDTO.getAssignedToId());
                assignedUser.ifPresent(task::setAssignedTo);
            }
            
            Task updatedTask = taskRepository.save(task);
            return Optional.of(convertToDTO(updatedTask));
        }
        
        return Optional.empty();
    }
    
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Optional<TaskDTO> updateTaskStatus(Long id, String status) {
        Optional<Task> existingTask = taskRepository.findById(id);
        
        if (existingTask.isPresent()) {
            try {
                Task.TaskStatus newStatus = Task.TaskStatus.valueOf(status.toUpperCase());
                Task task = existingTask.get();
                task.setStatus(newStatus);
                Task updatedTask = taskRepository.save(task);
                return Optional.of(convertToDTO(updatedTask));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
        
        return Optional.empty();
    }
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Contar tarefas por status
        Map<String, Long> tasksByStatus = new HashMap<>();
        for (Task.TaskStatus status : Task.TaskStatus.values()) {
            Long count = taskRepository.countByStatus(status);
            tasksByStatus.put(status.name(), count);
        }
        
        // Contar tarefas por usuário
        List<Object[]> tasksByUser = taskRepository.countTasksByUser();
        List<Map<String, Object>> userTaskStats = new ArrayList<>();
        
        for (Object[] result : tasksByUser) {
            Map<String, Object> userStat = new HashMap<>();
            userStat.put("userName", result[0]);
            userStat.put("taskCount", result[1]);
            userTaskStats.add(userStat);
        }
        
        stats.put("tasksByStatus", tasksByStatus);
        stats.put("tasksByUser", userTaskStats);
        stats.put("totalTasks", taskRepository.count());
        
        return stats;
    }
    
    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setDueDate(task.getDueDate());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setCreatedBy(task.getCreatedBy());
        
        if (task.getAssignedTo() != null) {
            dto.setAssignedToId(task.getAssignedTo().getId());
            dto.setAssignedToName(task.getAssignedTo().getName());
        }
        
        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getId());
            dto.setProjectName(task.getProject().getName());
        }
        
        return dto;
    }
    
    private List<TaskDTO> convertToDTOList(List<Task> tasks) {
        List<TaskDTO> dtos = new ArrayList<>();
        for (Task task : tasks) {
            dtos.add(convertToDTO(task));
        }
        return dtos;
    }
}