package com.techalves.taskmanager.service;

import com.techalves.taskmanager.dto.ProjectDTO;
import com.techalves.taskmanager.model.Project;
import com.techalves.taskmanager.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return convertToDTOList(projects);
    }
    
    public List<ProjectDTO> getActiveProjects() {
        List<Project> projects = projectRepository.findActiveProjects();
        return convertToDTOList(projects);
    }
    
    public Optional<ProjectDTO> getProjectById(Long id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.map(this::convertToDTO);
    }
    
    public ProjectDTO createProject(ProjectDTO projectDTO, String createdBy) {
        Project project = new Project();
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setStatus(Project.ProjectStatus.ACTIVE);
        project.setCreatedBy(createdBy);
        
        Project savedProject = projectRepository.save(project);
        return convertToDTO(savedProject);
    }
    
    public Optional<ProjectDTO> updateProject(Long id, ProjectDTO projectDTO) {
        Optional<Project> existingProject = projectRepository.findById(id);
        
        if (existingProject.isPresent()) {
            Project project = existingProject.get();
            project.setName(projectDTO.getName());
            project.setDescription(projectDTO.getDescription());
            project.setStartDate(projectDTO.getStartDate());
            project.setEndDate(projectDTO.getEndDate());
            
            if (projectDTO.getStatus() != null) {
                project.setStatus(projectDTO.getStatus());
            }
            
            Project updatedProject = projectRepository.save(project);
            return Optional.of(convertToDTO(updatedProject));
        }
        
        return Optional.empty();
    }
    
    public boolean deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Optional<ProjectDTO> updateProjectStatus(Long id, String status) {
        Optional<Project> existingProject = projectRepository.findById(id);
        
        if (existingProject.isPresent()) {
            try {
                Project.ProjectStatus newStatus = Project.ProjectStatus.valueOf(status.toUpperCase());
                Project project = existingProject.get();
                project.setStatus(newStatus);
                Project updatedProject = projectRepository.save(project);
                return Optional.of(convertToDTO(updatedProject));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
        
        return Optional.empty();
    }
    
    public List<Map<String, Object>> getProjectsWithStats() {
        List<Object[]> results = projectRepository.findProjectsWithTaskCount();
        List<Map<String, Object>> projectStats = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> stat = new HashMap<>();
            Project project = (Project) result[0];
            Long taskCount = (Long) result[1];
            
            stat.put("project", convertToDTO(project));
            stat.put("taskCount", taskCount != null ? taskCount : 0);
            projectStats.add(stat);
        }
        
        return projectStats;
    }
    
    private ProjectDTO convertToDTO(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setStartDate(project.getStartDate());
        dto.setEndDate(project.getEndDate());
        dto.setStatus(project.getStatus());
        dto.setCreatedAt(project.getCreatedAt());
        dto.setCreatedBy(project.getCreatedBy());
        
        if (project.getTasks() != null) {
            dto.setTaskCount((long) project.getTasks().size());
        } else {
            dto.setTaskCount(0L);
        }
        
        return dto;
    }
    
    private List<ProjectDTO> convertToDTOList(List<Project> projects) {
        List<ProjectDTO> dtos = new ArrayList<>();
        for (Project project : projects) {
            dtos.add(convertToDTO(project));
        }
        return dtos;
    }
}