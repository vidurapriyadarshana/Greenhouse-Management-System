package com.example.sensor_service.controller;

import com.example.sensor_service.common.CommonResponse;
import com.example.sensor_service.dto.DeviceRequestDTO;
import com.example.sensor_service.dto.DeviceResponseDTO;
import com.example.sensor_service.dto.TelemetryResponseDTO;
import com.example.sensor_service.service.SensorService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/sensors")
public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @GetMapping("/latest")
    public ResponseEntity<CommonResponse<Map<String, TelemetryResponseDTO>>> getLatestReadings() {
        Map<String, TelemetryResponseDTO> readings = sensorService.getLatestReadings();
        CommonResponse<Map<String, TelemetryResponseDTO>> response = new CommonResponse<>(
            200, "OK", "Latest sensor readings fetched successfully", readings
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/devices")
    public ResponseEntity<CommonResponse<DeviceResponseDTO>> registerDevice(
            @RequestBody DeviceRequestDTO request) {
        DeviceResponseDTO device = sensorService.registerDevice(request);
        CommonResponse<DeviceResponseDTO> response = new CommonResponse<>(
            201, "CREATED", "Device registered successfully", device
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
