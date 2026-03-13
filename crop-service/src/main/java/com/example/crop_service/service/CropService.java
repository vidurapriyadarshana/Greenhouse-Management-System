package com.example.crop_service.service;

import com.example.crop_service.dto.CropRequestDTO;
import com.example.crop_service.dto.CropResponseDTO;
import com.example.crop_service.dto.CropStatusUpdateDTO;

import java.util.List;

public interface CropService {

    CropResponseDTO createCrop(CropRequestDTO request);

    CropResponseDTO updateCropStatus(String id, CropStatusUpdateDTO request);

    List<CropResponseDTO> getAllCrops();
}
