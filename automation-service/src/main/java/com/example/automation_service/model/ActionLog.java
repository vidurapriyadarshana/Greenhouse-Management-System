package com.example.automation_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "action_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String zoneId;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private Double currentTemp;

    private Double minTemp;
    private Double maxTemp;

    @Column(nullable = false)
    private LocalDateTime triggeredAt;

    @PrePersist
    protected void onCreate() {
        triggeredAt = LocalDateTime.now();
    }
}
