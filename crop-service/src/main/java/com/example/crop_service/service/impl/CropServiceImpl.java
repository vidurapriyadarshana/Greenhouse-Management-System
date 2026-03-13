package com.example.crop_service.service.impl;

import com.example.crop_service.dto.CropRequestDTO;
import com.example.crop_service.dto.CropResponseDTO;
import com.example.crop_service.dto.CropStatusUpdateDTO;
import com.example.crop_service.exception.CropNotFoundException;
import com.example.crop_service.exception.InvalidStatusTransitionException;
import com.example.crop_service.model.Crop;
import com.example.crop_service.model.CropStatus;
import com.example.crop_service.repository.CropRepository;
import com.example.crop_service.service.CropService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CropServiceImpl implements CropService {

    private static final Logger logger = LoggerFactory.getLogger(CropServiceImpl.class);

    private final CropRepository cropRepository;

    public CropServiceImpl(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    @Override
    public CropResponseDTO createCrop(CropRequestDTO request) {
        logger.info("Creating crop batch: {} in zone: {}", request.getName(), request.getZoneId());

        Crop crop = new Crop();
        crop.setName(request.getName());
        crop.setZoneId(request.getZoneId());
        crop.setQuantity(request.getQuantity());

        crop = cropRepository.save(crop);
        return mapToDTO(crop);
    }

    @Override
    public CropResponseDTO updateCropStatus(String id, CropStatusUpdateDTO request) {
        logger.info("Updating crop status for id: {} to: {}", id, request.getStatus());

        Crop crop = cropRepository.findById(id)
            .orElseThrow(() -> new CropNotFoundException("Crop not found with id: " + id));

        CropStatus newStatus;
        try {
            newStatus = CropStatus.valueOf(request.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusTransitionException(
                "Invalid status: " + request.getStatus()
                    + ". Valid values: SEEDLING, VEGETATIVE, HARVESTED");
        }

        CropStatus currentStatus = crop.getStatus();

        if (currentStatus == CropStatus.SEEDLING && newStatus != CropStatus.VEGETATIVE) {
            throw new InvalidStatusTransitionException(
                "SEEDLING can only transition to VEGETATIVE. Attempted: " + newStatus);
        }
        if (currentStatus == CropStatus.VEGETATIVE && newStatus != CropStatus.HARVESTED) {
            throw new InvalidStatusTransitionException(
                "VEGETATIVE can only transition to HARVESTED. Attempted: " + newStatus);
        }
        if (currentStatus == CropStatus.HARVESTED) {
            throw new InvalidStatusTransitionException(
                "HARVESTED is the final state. No further transitions allowed.");
        }

        crop.setStatus(newStatus);
        crop = cropRepository.save(crop);

        logger.info("Crop {} transitioned from {} to {}", id, currentStatus, newStatus);
        return mapToDTO(crop);
    }

    @Override
    public List<CropResponseDTO> getAllCrops() {
        logger.info("Fetching all crops");
        return cropRepository.findAll().stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private CropResponseDTO mapToDTO(Crop crop) {
        CropResponseDTO dto = new CropResponseDTO();
        dto.setId(crop.getId());
        dto.setName(crop.getName());
        dto.setZoneId(crop.getZoneId());
        dto.setQuantity(crop.getQuantity());
        dto.setStatus(crop.getStatus().name());
        dto.setPlantedAt(crop.getPlantedAt());
        dto.setUpdatedAt(crop.getUpdatedAt());
        return dto;
    }
}
