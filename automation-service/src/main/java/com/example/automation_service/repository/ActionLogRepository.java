package com.example.automation_service.repository;

import com.example.automation_service.model.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActionLogRepository extends JpaRepository<ActionLog, String> {
    List<ActionLog> findAllByOrderByTriggeredAtDesc();
}
