package com.example.sensor_service.service;

import com.example.sensor_service.dto.DeviceRequestDTO;
import com.example.sensor_service.dto.DeviceResponseDTO;
import com.example.sensor_service.dto.TelemetryResponseDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class SensorService {

    private static final Logger logger = LoggerFactory.getLogger(SensorService.class);

    private final ExternalIoTService externalIoTService;

    private final ConcurrentHashMap<String, TelemetryResponseDTO> latestReadings =
        new ConcurrentHashMap<>();

    public SensorService(ExternalIoTService externalIoTService) {
        this.externalIoTService = externalIoTService;
    }

    public DeviceResponseDTO registerDevice(DeviceRequestDTO request) {
        logger.info("Registering device via external IoT API: {}", request.getName());
        return externalIoTService.registerDevice(request);
    }

    public void storeLatestReading(TelemetryResponseDTO telemetry) {
        latestReadings.put(telemetry.getDeviceId(), telemetry);
    }

    public ConcurrentHashMap<String, TelemetryResponseDTO> getLatestReadings() {
        return latestReadings;
    }
}
