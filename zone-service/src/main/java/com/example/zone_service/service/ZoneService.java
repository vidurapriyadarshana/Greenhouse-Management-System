package com.example.zone_service.service;

import com.example.zone_service.dto.DeviceRegistrationDTO;
import com.example.zone_service.dto.ZoneRequestDTO;
import com.example.zone_service.dto.ZoneResponseDTO;
import com.example.zone_service.exception.InvalidThresholdException;
import com.example.zone_service.exception.ZoneNotFoundException;
import com.example.zone_service.exception.DeviceRegistrationException;
import com.example.zone_service.feign.IoTIntegrationClient;
import com.example.zone_service.model.Zone;
import com.example.zone_service.repository.ZoneRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ZoneService {

    private static final Logger logger = LoggerFactory.getLogger(ZoneService.class);

    private final ZoneRepository zoneRepository;
    private final IoTIntegrationClient ioTIntegrationClient;

    public ZoneService(ZoneRepository zoneRepository, IoTIntegrationClient ioTIntegrationClient) {
        this.zoneRepository = zoneRepository;
        this.ioTIntegrationClient = ioTIntegrationClient;
    }

    public ZoneResponseDTO createZone(ZoneRequestDTO request) {
        logger.info("Creating zone with name: {}", request.getName());

        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new InvalidThresholdException("minTemp must be strictly less than maxTemp");
        }

        Zone zone = new Zone();
        zone.setName(request.getName());
        zone.setMinTemp(request.getMinTemp());
        zone.setMaxTemp(request.getMaxTemp());
        zone.setMinHumidity(request.getMinHumidity());
        zone.setMaxHumidity(request.getMaxHumidity());
        zone = zoneRepository.save(zone);

        try {
            DeviceRegistrationDTO deviceDTO = new DeviceRegistrationDTO();
            deviceDTO.setName(request.getName() + "-Sensor");
            deviceDTO.setZoneId(zone.getId());

            Map<String, Object> deviceResponse = ioTIntegrationClient.registerDevice(deviceDTO);
            String deviceId = (String) deviceResponse.get("deviceId");
            zone.setDeviceId(deviceId);
            zone = zoneRepository.save(zone);
            logger.info("Device registered successfully with deviceId: {}", deviceId);
        } catch (Exception e) {
            logger.error("Failed to register device for zone: {}", zone.getId(), e);
            throw new DeviceRegistrationException("Failed to register device with IoT service: " + e.getMessage());
        }

        return mapToResponseDTO(zone);
    }

    public ZoneResponseDTO getZoneById(String id) {
        logger.info("Fetching zone with id: {}", id);
        Zone zone = zoneRepository.findById(id)
            .orElseThrow(() -> new ZoneNotFoundException("Zone not found with id: " + id));
        return mapToResponseDTO(zone);
    }

    public ZoneResponseDTO updateZone(String id, ZoneRequestDTO request) {
        logger.info("Updating zone with id: {}", id);

        if (request.getMinTemp() >= request.getMaxTemp()) {
            throw new InvalidThresholdException("minTemp must be strictly less than maxTemp");
        }

        Zone zone = zoneRepository.findById(id)
            .orElseThrow(() -> new ZoneNotFoundException("Zone not found with id: " + id));

        zone.setName(request.getName());
        zone.setMinTemp(request.getMinTemp());
        zone.setMaxTemp(request.getMaxTemp());
        zone.setMinHumidity(request.getMinHumidity());
        zone.setMaxHumidity(request.getMaxHumidity());
        zone = zoneRepository.save(zone);

        return mapToResponseDTO(zone);
    }

    public void deleteZone(String id) {
        logger.info("Deleting zone with id: {}", id);
        Zone zone = zoneRepository.findById(id)
            .orElseThrow(() -> new ZoneNotFoundException("Zone not found with id: " + id));
        zoneRepository.delete(zone);
    }

    public List<ZoneResponseDTO> getAllZones() {
        logger.info("Fetching all zones");
        return zoneRepository.findAll().stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    private ZoneResponseDTO mapToResponseDTO(Zone zone) {
        ZoneResponseDTO dto = new ZoneResponseDTO();
        dto.setId(zone.getId());
        dto.setName(zone.getName());
        dto.setMinTemp(zone.getMinTemp());
        dto.setMaxTemp(zone.getMaxTemp());
        dto.setMinHumidity(zone.getMinHumidity());
        dto.setMaxHumidity(zone.getMaxHumidity());
        dto.setDeviceId(zone.getDeviceId());
        dto.setCreatedAt(zone.getCreatedAt());
        dto.setUpdatedAt(zone.getUpdatedAt());
        return dto;
    }
}
