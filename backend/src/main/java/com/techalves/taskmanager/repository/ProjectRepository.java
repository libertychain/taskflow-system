package com.techalves.taskmanager.repository;

import com.techalves.taskmanager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    List<Project> findByStatus(Project.ProjectStatus status);
    
    @Query("SELECT p FROM Project p WHERE p.status = 'ACTIVE'")
    List<Project> findActiveProjects();
    
    @Query("SELECT p, COUNT(t) FROM Project p LEFT JOIN p.tasks t GROUP BY p")
    List<Object[]> findProjectsWithTaskCount();
}