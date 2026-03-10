package com.example.sensor_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponseDTO {
    private String deviceId;
    private String name;
    private String zoneId;
    private String userId;
    private Instant createAt;
}
