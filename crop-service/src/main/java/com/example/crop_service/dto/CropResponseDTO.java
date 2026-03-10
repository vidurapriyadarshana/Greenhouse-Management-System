package com.example.crop_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropResponseDTO {
    private String id;
    private String name;
    private String zoneId;
    private Integer quantity;
    private String status;
    private LocalDateTime plantedAt;
    private LocalDateTime updatedAt;
}
