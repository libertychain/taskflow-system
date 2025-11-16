package com.techalves.taskmanager.dto;

import com.techalves.taskmanager.model.Project;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Project.ProjectStatus status;
    private LocalDateTime createdAt;
    private String createdBy;
    private Long taskCount;
    
    public ProjectDTO() {}
    
    public ProjectDTO(Long id, String name, String description, LocalDate startDate, 
                      LocalDate endDate, Project.ProjectStatus status, 
                      LocalDateTime createdAt, String createdBy, Long taskCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.taskCount = taskCount;
    }
    
    // Getters e Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public Project.ProjectStatus getStatus() {
        return status;
    }
    
    public void setStatus(Project.ProjectStatus status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public Long getTaskCount() {
        return taskCount;
    }
    
    public void setTaskCount(Long taskCount) {
        this.taskCount = taskCount;
    }
}