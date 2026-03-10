package com.example.automation_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryInputDTO {
    private String deviceId;
    private String zoneId;
    private TelemetryValueDTO value;
    private Instant capturedAt;
}
