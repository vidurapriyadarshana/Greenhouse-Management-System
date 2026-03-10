package com.example.zone_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRegistrationDTO {
    private String name;
    private String zoneId;
}
