package com.example.crop_service.controller;

import com.example.crop_service.common.CommonResponse;
import com.example.crop_service.dto.CropRequestDTO;
import com.example.crop_service.dto.CropResponseDTO;
import com.example.crop_service.dto.CropStatusUpdateDTO;
import com.example.crop_service.service.CropService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse<CropResponseDTO>> createCrop(
            @RequestBody CropRequestDTO request) {
        CropResponseDTO crop = cropService.createCrop(request);
        CommonResponse<CropResponseDTO> response = new CommonResponse<>(
            201, "CREATED", "Crop batch registered successfully", crop
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CommonResponse<CropResponseDTO>> updateCropStatus(
            @PathVariable String id,
            @RequestBody CropStatusUpdateDTO request) {
        CropResponseDTO crop = cropService.updateCropStatus(id, request);
        CommonResponse<CropResponseDTO> response = new CommonResponse<>(
            200, "OK", "Crop status updated successfully", crop
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<CropResponseDTO>>> getAllCrops() {
        List<CropResponseDTO> crops = cropService.getAllCrops();
        CommonResponse<List<CropResponseDTO>> response = new CommonResponse<>(
            200, "OK", "Crop inventory fetched successfully", crops
        );
        return ResponseEntity.ok(response);
    }
}
