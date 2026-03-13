package com.example.sensor_service.service;

import com.example.sensor_service.dto.DeviceRequestDTO;
import com.example.sensor_service.dto.DeviceResponseDTO;
import com.example.sensor_service.dto.TelemetryResponseDTO;

import java.util.Map;

public interface SensorService {

    DeviceResponseDTO registerDevice(DeviceRequestDTO request);

    void storeLatestReading(TelemetryResponseDTO telemetry);

    Map<String, TelemetryResponseDTO> getLatestReadings();
}
