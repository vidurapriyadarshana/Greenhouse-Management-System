package com.example.zone_service.service;

import com.example.zone_service.dto.ZoneRequestDTO;
import com.example.zone_service.dto.ZoneResponseDTO;

import java.util.List;

public interface ZoneService {

    ZoneResponseDTO createZone(ZoneRequestDTO request);

    ZoneResponseDTO getZoneById(String id);

    ZoneResponseDTO updateZone(String id, ZoneRequestDTO request);

    void deleteZone(String id);

    List<ZoneResponseDTO> getAllZones();
}
