package com.example.zone_service.controller;

import com.example.zone_service.common.CommonResponse;
import com.example.zone_service.dto.ZoneRequestDTO;
import com.example.zone_service.dto.ZoneResponseDTO;
import com.example.zone_service.service.ZoneService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

    private final ZoneService zoneService;

    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @PostMapping
    public ResponseEntity<CommonResponse<ZoneResponseDTO>> createZone(@RequestBody ZoneRequestDTO request) {
        ZoneResponseDTO zone = zoneService.createZone(request);
        CommonResponse<ZoneResponseDTO> response = new CommonResponse<>(
            201, "CREATED", "Zone created successfully", zone
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ZoneResponseDTO>> getZoneById(@PathVariable String id) {
        ZoneResponseDTO zone = zoneService.getZoneById(id);
        CommonResponse<ZoneResponseDTO> response = new CommonResponse<>(
            200, "OK", "Zone fetched successfully", zone
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<ZoneResponseDTO>> updateZone(
            @PathVariable String id,
            @RequestBody ZoneRequestDTO request) {
        ZoneResponseDTO zone = zoneService.updateZone(id, request);
        CommonResponse<ZoneResponseDTO> response = new CommonResponse<>(
            200, "OK", "Zone updated successfully", zone
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> deleteZone(@PathVariable String id) {
        zoneService.deleteZone(id);
        CommonResponse<Void> response = new CommonResponse<>(
            204, "NO_CONTENT", "Zone deleted successfully", null
        );
        return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ZoneResponseDTO>>> getAllZones() {
        List<ZoneResponseDTO> zones = zoneService.getAllZones();
        CommonResponse<List<ZoneResponseDTO>> response = new CommonResponse<>(
            200, "OK", "Zones fetched successfully", zones
        );
        return ResponseEntity.ok(response);
    }
}
