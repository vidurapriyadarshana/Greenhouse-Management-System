package com.example.automation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActionLogResponseDTO {
    private String id;
    private String zoneId;
    private String deviceId;
    private String action;
    private Double currentTemp;
    private Double minTemp;
    private Double maxTemp;
    private LocalDateTime triggeredAt;
}
