package com.example.zone_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneResponseDTO {
    private String id;
    private String name;
    private Double minTemp;
    private Double maxTemp;
    private Double minHumidity;
    private Double maxHumidity;
    private String deviceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
