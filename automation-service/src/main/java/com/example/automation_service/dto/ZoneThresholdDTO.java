package com.example.automation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneThresholdDTO {
    private String id;
    private String name;
    private Double minTemp;
    private Double maxTemp;
    private Double minHumidity;
    private Double maxHumidity;
    private String deviceId;
}
