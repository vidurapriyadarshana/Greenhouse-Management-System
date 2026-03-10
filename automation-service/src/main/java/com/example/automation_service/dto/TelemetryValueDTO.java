package com.example.automation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryValueDTO {
    private Double temperature;
    private String tempUnit;
    private Double humidity;
    private String humidityUnit;
}
