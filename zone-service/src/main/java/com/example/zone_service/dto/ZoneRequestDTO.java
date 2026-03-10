package com.example.zone_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneRequestDTO {
    private String name;
    private Double minTemp;
    private Double maxTemp;
    private Double minHumidity;
    private Double maxHumidity;
}
