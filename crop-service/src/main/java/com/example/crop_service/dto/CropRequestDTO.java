package com.example.crop_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropRequestDTO {
    private String name;
    private String zoneId;
    private Integer quantity;
}
